# BB84 Simulator
 Dokumentacja składa się z opisu programu. Interface jest intuicyjne, niemniej jego działanie opisane jest w sekcji
 "klasa SimulationQKD" w linii 42.
 
 Program składa się z klasy głównej Main oraz 4 klas obsługujących funkcjonalności, tj.: 
 Qubit, SimulationQKD, Person, Strategy
 
 klasa Main:
    Zawiera metodę main(String[] args) {}, która uruchamia program. 
    Działanie klasy Main ogranicza się do utworzenia nowej instancji klasy SimulationQKD oraz wywołania metody run
 
 klasa Qubit:
    Odpowiada za symulowanie kwantowego kubitu w interpretacji spolaryzowanych fotonów. 
    Zawiera 2 wielkości:  base - wybór bazy: prosta, skośna
                          value - wybór polaryzacji w danej bazie
 
 klasa Strategy:
    Dotyczy osoby podsłuchującej, Eve i umożliwia jej strategię podsłuchiwania:  ALL - podsłuchuje wszystko
                                                                                 SERIES - podsłuchuje seriami (część 
                                                                                 oryginalnej wiadomości pozostanie 
                                                                                 nienaruszona)
                                                                                
 klasa Person:
    Umożliwia "stworzenie" osób biorących udziału w komunikacji, tj. Alice -> (Eve) -> Bob
    Konstruktor osoby przyjmuje argumenty, takie jak: size - odpowiada za długość przesyłanego łańcucha
                                                      detectorNoise - odpowiada za wprowadzenie zadanego poziomu szumu
                                                      isBadGuy - ustala czy osoba biorąca udział w komunikacji jest 
                                                                 autoryzowana czy podsłuchuje
                                                      STRATEGY - dotyczy osoby podsłuchującej 
    Kolejno występują metody odpowedzialne za: losowanie liczb losowych do tabeli
                                               ustalanie losowej bazy
                                               dekodowanie przez Boba lub Eve + wprowadzanie szumu 
                                                    strategia ALL (zawsze Bob lub gdy wybierze Eve)
                                                    strategia SERIES (tylko Eve) 
                                               kodowanie przez Alice lub Eve
                                                     warunek if (Alice lub gdy Eve wybierze strategię ALL)
                                                     warunek else (tylko Eve gdy wybierze strategię SERIES)
                                               wypisanie informacji w kolejności:   Person
                                                                                    Key
                                                                                    Base
 
 klasa SimulationQKD:
    Na początku zdefiniowane są metody odpowiedzialne za porównywanie kluczy i baz. 
    Następnie intuicyjny interface pobiera od użytkownika dane dotyczące symulacji:     strategia podsłuchiwania
                                                                                        poziom detekcji szumu
                                                                                        rozmiar "surowego" klucza                                                              
    Następnie inicjalizowane są obiekty klasy Person, tj. Alice, Eve, Bob, które pzyjmują parametry podane przez 
    użytkownika.
    Następuje symulacja, wynikiem której są informacje o jej przebiegu, np.:
        ---------------------------------------------------------------------------------------------------------------
        Alice has generated random raw key and encoded it in random bases:
        Alice: Person{key=[1, 0, 0, 1, 0, 0, 0, 1, 0, 1], base=[skośna, prosta, prosta, skośna, prosta, prosta, ...
        Eva eavesdropped using all qubits strategy, and decoded Alice's raw key in random bases:
        Eva:  Person{key=[1, 1, 0, 1, 0, 0, 0, 1, 0, 1], base=[skośna, skośna, skośna, prosta, skośna, prosta, ...
        Bob received from Eva corrupted raw key, and decoded it
        Bob:  Person{key=[1, 1, 0, 0, 0, 0, 0, 1, 1, 1], base=[prosta, skośna, skośna, skośna, skośna, prosta, ...
        Eve has guessed 90.0% of Alice's key
        Bob has guessed 70.0% of Alice's key
        Establishing indexes of bits that was measured in the same bases: [false, false, false, true, false, true, ...
        Establishing keys from raw keys: 
        Alice's key: [1, 0, 0, 1]
        Bob's key:  [0, 0, 1, 1]
        Eva's key:  [1, 0, 0, 1]
        Eve posses 100.0% of Alice's key
        Bob posses 50.0% of Alice's key
    
        Thanks for running this simulation. It may be upgraded with Key Distillation phase in future.
    
        Process finished with exit code 0
        ----------------------------------------------------------------------------------------------------------------
                                                                                    