# Proiect Programare Web cu Java

1. Ca utilizator pot cauta produse pe site
2. Ca utilizator pot vizualiza produsul intr-o pagina dedicata
3. Ca utilizator pot filtra produsele dupa categorii
4. Ca utilizator ma pot inregistra pe site
5. Ca utilizator ma pot loga pe site
6. Ca utilizator pot adauga produsele la favorite
7. Produsele vor avea stoc limitat
8. Produsele vor putea fi adaugate, editate sau sterse din interfata
9. Utilizatorii conectati la platforma pot lasa recenzii produselor
10. Utilizatorii vor fi de 3 feluri -> neinregistrat, inregistrat si moderator

### De la aceste cerinte se vor implementa urmatoarele cerinte:

#### Utilizatorii pot interactiona cu aplicatia folosind sistemul de roluri
   * neinregistrat 
     * poate cauta produse
     * potate vizualiza un produs
     * poate vizualiza lista de produse
     * poate afisa produsele in functie de categorie
     * poate vedea review-urile produselor
     * se poate loga/inregistra
   * inregistrat
     * poate adauga review
     * poate adauga/sterge si afisa produse favorite
     * poate cumpara un produs
   * moderator
     * poate adauga/edita sau sterge un produs
   
---
#### DB diagram
![db](db.png)

#### Tests Coverage
![coverage](coverage.png)

