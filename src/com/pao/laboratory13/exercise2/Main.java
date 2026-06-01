package com.pao.laboratory13.exercise2;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Bonus — Demo socket multi-client care ruleaza motorul de protocol din ex1.
 * Serverul si clientii ruleaza in acelasi proces pe thread-uri separate.
 */
public class Main {

    private enum State { INIT, AUTH, OPEN, CLOSED }

    static class Session {
        private State state = State.INIT;
        private int historyCount = 0;

        String handle(String line) {
            String[] tok = line.split("\\s+", 2);
            String cmd = tok[0].toUpperCase();
            String rest = tok.length > 1 ? tok[1] : "";
            switch (cmd) {
                case "AUTH":     return handleAuth(rest);
                case "OPEN":     return handleOpen(rest);
                case "SEND":     return handleSend(rest);
                case "BROADCAST":return handleBroadcast(rest);
                case "HISTORY":  return handleHistory(rest);
                case "CLOSE":    return handleClose(rest);
                default:         return "ERR E_PARSE UNKNOWN_COMMAND";
            }
        }

        private String handleAuth(String rest) {
            if (state == State.CLOSED) return "ERR E_STATE CLOSED";
            if (rest.isEmpty()) return "ERR E_PARSE AUTH";
            state = State.AUTH; historyCount = 0;
            return "OK AUTH user=" + rest.split("\\s+")[0];
        }
        private String handleOpen(String rest) {
            if (state == State.CLOSED) return "ERR E_STATE CLOSED";
            if (!rest.isEmpty()) return "ERR E_PARSE OPEN";
            if (state == State.OPEN)  return "ERR E_STATE ALREADY_OPEN";
            if (state == State.INIT)  return "ERR E_STATE NOT_OPEN";
            state = State.OPEN; return "OK OPEN";
        }
        private String handleSend(String rest) {
            if (state == State.CLOSED) return "ERR E_STATE CLOSED";
            if (rest.isEmpty()) return "ERR E_PARSE SEND";
            if (state != State.OPEN) return "ERR E_STATE NOT_OPEN";
            historyCount++; return "OK OPEN sent";
        }
        private String handleBroadcast(String rest) {
            if (state == State.CLOSED) return "ERR E_STATE CLOSED";
            if (rest.isEmpty()) return "ERR E_PARSE BROADCAST";
            if (state != State.OPEN) return "ERR E_STATE NOT_OPEN";
            historyCount++; return "OK OPEN broadcast";
        }
        private String handleHistory(String rest) {
            if (state == State.CLOSED) return "ERR E_STATE CLOSED";
            if (!rest.isEmpty()) return "ERR E_PARSE HISTORY";
            if (state != State.OPEN) return "ERR E_STATE NOT_OPEN";
            return "OK OPEN history=" + historyCount;
        }
        private String handleClose(String rest) {
            if (state == State.CLOSED) return "ERR E_STATE CLOSED";
            if (!rest.isEmpty()) return "ERR E_PARSE CLOSE";
            if (state != State.OPEN) return "ERR E_STATE NOT_OPEN";
            state = State.CLOSED; return "OK CLOSED";
        }
    }

    public static void main(String[] args) throws Exception {
        int port = 9000;
        CountDownLatch serverReady = new CountDownLatch(1);
        CountDownLatch clientsDone = new CountDownLatch(2);
        ExecutorService pool = Executors.newCachedThreadPool();

        // Server thread
        pool.submit(() -> {
            try (ServerSocket ss = new ServerSocket(port)) {
                System.out.println("[SERVER] Listening on port " + port);
                serverReady.countDown();
                for (int i = 0; i < 2; i++) {
                    Socket sock = ss.accept();
                    int clientId = i + 1;
                    pool.submit(() -> {
                        try (sock;
                             BufferedReader in = new BufferedReader(new InputStreamReader(sock.getInputStream()));
                             PrintWriter out = new PrintWriter(sock.getOutputStream(), true)) {
                            System.out.println("[CLIENT-" + clientId + "] Connected");
                            Session session = new Session();
                            String line;
                            while ((line = in.readLine()) != null) {
                                String resp = session.handle(line);
                                System.out.println("[CLIENT-" + clientId + "] >> " + line + "  =>  " + resp);
                                out.println(resp);
                            }
                        } catch (IOException e) {
                            System.out.println("[CLIENT-" + clientId + "] Error: " + e.getMessage());
                        } finally {
                            System.out.println("[CLIENT-" + clientId + "] Disconnected");
                            clientsDone.countDown();
                        }
                    });
                }
                clientsDone.await();
                System.out.println("[SERVER] All clients done. Shutting down.");
            } catch (Exception e) {
                System.out.println("[SERVER] Error: " + e.getMessage());
            }
        });

        serverReady.await();

        List<List<String>> scripts = List.of(
            List.of("AUTH alice", "OPEN", "SEND hello", "BROADCAST ping", "HISTORY", "CLOSE"),
            List.of("AUTH bob", "OPEN", "SEND world", "BROADCAST hi", "HISTORY", "CLOSE")
        );

        for (int i = 0; i < 2; i++) {
            int clientId = i + 1;
            List<String> script = scripts.get(i);
            pool.submit(() -> {
                try (Socket sock = new Socket("localhost", port);
                     BufferedReader in = new BufferedReader(new InputStreamReader(sock.getInputStream()));
                     PrintWriter out = new PrintWriter(sock.getOutputStream(), true)) {
                    for (String cmd : script) {
                        out.println(cmd);
                        in.readLine();
                    }
                } catch (IOException e) {
                    System.out.println("[CLIENT-" + clientId + "] client error: " + e.getMessage());
                }
            });
        }

        pool.shutdown();
        pool.awaitTermination(10, java.util.concurrent.TimeUnit.SECONDS);
    }
}
