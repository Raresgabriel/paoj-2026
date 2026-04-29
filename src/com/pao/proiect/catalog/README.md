# Proiect PAO - Catalog scolar

Autor: Bordei Rares-Gabriel
Tema: catalog pentru gimnaziu (clasele 5-8)
Etapa: I

## Ce face

Tine evidenta elevilor, profesorilor, materiilor, claselor si notelor.
Tot ce face e in memorie - persistenta vine la etapa 2.

## 10 actiuni

1. Adaugare elev nou
2. Adaugare profesor (cu grad didactic)
3. Adaugare materie
4. Inregistrare clasa cu diriginte
5. Asociere profesor cu materie
6. Acordare nota unui elev
7. Cautare elev dupa CNP
8. Listare elevi dintr-o clasa, sortati alfabetic
9. Calcul medie elev la o materie
10. Calcul medie generala elev

## Tipurile de obiecte (10)

- Persoana - clasa abstracta, baza pentru elev si profesor
- Elev
- Profesor
- Diriginte - extinde Profesor (al doilea nivel de mostenire)
- Materie
- Clasa
- Nota - imutabila
- AnScolar - imutabila
- TipNota - enum (TEZA, ORAL, EXTEMPORAL, REFERAT, TEMA)
- GradDidactic - enum (DEBUTANT, DEFINITIVAT, GRADUL_II, GRADUL_I)

Plus interfata Identificabil pe care o implementeaza tot ce are id (Persoana, Materie, Clasa).

## Structura

```
proiect/
  README.md
  src/com/pao/proiect/catalog/
    Main.java
    model/
    service/
    exception/
```

## Cum rulez

Din folderul `proiect`:

```
javac -d out src/com/pao/proiect/catalog/model/*.java src/com/pao/proiect/catalog/exception/*.java src/com/pao/proiect/catalog/service/*.java src/com/pao/proiect/catalog/Main.java
java -cp out com.pao.proiect.catalog.Main
```

Main-ul apeleaza pe rand toate cele 10 actiuni si afiseaza ce iese.
