package com.pao.laboratory12;

import com.pao.laboratory12.model.Author;
import com.pao.laboratory12.model.Book;
import com.pao.laboratory12.model.Loan;
import com.pao.laboratory12.model.Reader;
import com.pao.laboratory12.repository.AuthorRepository;
import com.pao.laboratory12.repository.BookRepository;
import com.pao.laboratory12.repository.LoanRepository;
import com.pao.laboratory12.repository.ReaderRepository;
import com.pao.laboratory12.util.DatabaseConnection;

import java.sql.Connection;
import java.sql.Statement;

public class Main {

    public static void main(String[] args) throws Exception {
        initSchema();

        AuthorRepository authorRepo = new AuthorRepository();
        BookRepository bookRepo = new BookRepository();
        ReaderRepository readerRepo = new ReaderRepository();
        LoanRepository loanRepo = new LoanRepository();

        // --- Authors ---
        Author a1 = new Author("Ion Creanga", "RO");
        Author a2 = new Author("Mihai Eminescu", "RO");
        authorRepo.save(a1);
        authorRepo.save(a2);
        System.out.println("Saved: " + a1);
        System.out.println("Saved: " + a2);

        a1.setCountry("Romania");
        authorRepo.update(a1);
        System.out.println("Updated: " + authorRepo.findById(a1.getId()).orElse(null));

        System.out.println("All authors: " + authorRepo.findAll());

        // --- Books ---
        Book b1 = new Book("Amintiri din copilarie", a1.getId());
        Book b2 = new Book("Luceafarul", a2.getId());
        bookRepo.save(b1);
        bookRepo.save(b2);
        System.out.println("Saved: " + b1);

        System.out.println("All books: " + bookRepo.findAll());

        // --- Readers ---
        Reader r1 = new Reader("Popescu Ion", "ion@test.com");
        readerRepo.save(r1);
        System.out.println("Saved: " + r1);

        // --- Loans ---
        Loan l1 = new Loan(b1.getId(), r1.getId(), "2026-06-01");
        loanRepo.save(l1);
        System.out.println("Saved: " + l1);

        b1.setAvailable(false);
        bookRepo.update(b1);
        System.out.println("Book loaned: " + bookRepo.findById(b1.getId()).orElse(null));

        // --- Return loan ---
        l1.setReturnDate("2026-06-15");
        loanRepo.update(l1);
        b1.setAvailable(true);
        bookRepo.update(b1);
        System.out.println("Loan returned: " + loanRepo.findById(l1.getId()).orElse(null));

        // --- Delete ---
        loanRepo.delete(l1.getId());
        System.out.println("All loans after delete: " + loanRepo.findAll());

        DatabaseConnection.getInstance().close();
    }

    private static void initSchema() throws Exception {
        Connection conn = DatabaseConnection.getInstance().getConnection();
        try (Statement st = conn.createStatement()) {
            st.execute("DROP TABLE IF EXISTS loan");
            st.execute("DROP TABLE IF EXISTS book");
            st.execute("DROP TABLE IF EXISTS reader");
            st.execute("DROP TABLE IF EXISTS author");
            st.execute("CREATE TABLE author (id INTEGER PRIMARY KEY AUTOINCREMENT, name VARCHAR(200) NOT NULL, country VARCHAR(100))");
            st.execute("CREATE TABLE book (id INTEGER PRIMARY KEY AUTOINCREMENT, title VARCHAR(300) NOT NULL, author_id INTEGER NOT NULL, available INTEGER NOT NULL DEFAULT 1, FOREIGN KEY (author_id) REFERENCES author(id))");
            st.execute("CREATE TABLE reader (id INTEGER PRIMARY KEY AUTOINCREMENT, name VARCHAR(200) NOT NULL, email VARCHAR(200))");
            st.execute("CREATE TABLE loan (id INTEGER PRIMARY KEY AUTOINCREMENT, book_id INTEGER NOT NULL, reader_id INTEGER NOT NULL, loan_date VARCHAR(20) NOT NULL, return_date VARCHAR(20), FOREIGN KEY (book_id) REFERENCES book(id), FOREIGN KEY (reader_id) REFERENCES reader(id))");
        }
    }
}
