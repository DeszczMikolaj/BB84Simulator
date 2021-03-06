package com.company;

import java.util.Arrays;
import java.util.Random;

public class Person {
    public int[] key ;
    public String[] base;
    public static int size;
    private static int detectorNoise;
    private boolean isBadGuy;
    private  Object STRATEGY; // dotyczy Eve, osoby podsłuchującej - czy podsłuchuje wszystko czy seriami
    private Qubit[] recivedQubits;

    //Bob i Alice - konstruktor
    public Person(int size, int detectorNoise, boolean isBadGuy) {
        Person.size = size;
        base = new String[size];
        key = new int[size];
        Person.detectorNoise = detectorNoise;
        this.isBadGuy = isBadGuy;
    }

    //Eve (bo umożliwia wybór strategii podsłuchiwania) - konstruktor
    public Person(int size, int detectorNoise, boolean isBadGuy, Object STRATEGY) {
        Person.size = size;
        base = new String[size];
        key = new int[size];
        Person.detectorNoise = detectorNoise;
        this.isBadGuy = isBadGuy;
        this.STRATEGY = STRATEGY;
    }

    //losowanie liczb losowych do tabeli
    public int[] getRandomNumbers(int bound , int size){
        int[] randomNumberTable = new int[size];
        Random r = new Random();
        for(int i=0; i<size;i++){
            randomNumberTable[i] = r.nextInt(bound + 1) ;
        }
        return randomNumberTable;
    }

    //ustalanie losowej bazy
    public void setBase(){
        int[] randomBits = getRandomNumbers(1,size);
        for(int i=0; i<size;i++){
            if(randomBits[i] == 0) this.base[i] = "prosta";
            else this.base[i] = "skośna";
        }
    }

    //dekodowanie przez Boba lub Eve
    public int[] decode(Qubit[] encryptedBits) {
        setBase();
        recivedQubits=encryptedBits;
        //strategia ALL
        if (!isBadGuy || STRATEGY == Strategy.ALL) {
            for (int i = 0; i < size; i++) {
                if (base[i] == encryptedBits[i].base) {
                    key[i] = encryptedBits[i].value;
                } else {
                    int[] randomNumber = getRandomNumbers(1, 1);
                    key[i] = randomNumber[0];
                }
                int[] randomNumber = getRandomNumbers(100, 1);
                if (randomNumber[0] < detectorNoise) {
                    if (key[i] == 1) key[i] = 0;
                    else key[i] = 1;
                }
            }
        //strategia SERIES
        } else if (STRATEGY == Strategy.SERIES) {
            final int howManyInOneSerie = size / 10;
            final int spaceBeetwenSeries = 2 * howManyInOneSerie;
            int counterForHowMany = 0;
            int counterForSpace = 0;
            for (int i = 0; i < size; i++) {
                if (counterForHowMany < howManyInOneSerie) {
                    if (base[i] == encryptedBits[i].base) {
                        key[i] = encryptedBits[i].value;
                    } else {
                        int[] randomNumber = getRandomNumbers(1, 1);
                        key[i] = randomNumber[0];
                    }
                    int[] randomNumber = getRandomNumbers(100, 1);
                    if (randomNumber[0] < detectorNoise) {
                        if (key[i] == 1) key[i] = 0;
                        else key[i] = 1;
                    }
                    counterForHowMany++;
                } else {
                    key[i] = -1;
                    counterForSpace++;
                }
                if (counterForSpace == spaceBeetwenSeries) {
                    counterForHowMany = 0;
                    counterForSpace = 0;
                }
            }
        }
        return key;
    }

    //kodowanie przez Alice lub Eve
    public Qubit[] encode(){
        Qubit[] encodedBits = new Qubit[size];
        if (!isBadGuy || STRATEGY == Strategy.ALL) {
            for (int i = 0; i < size; i++) {
                encodedBits[i] = new Qubit(base[i], key[i]);
            }
        }
        else {
            for (int i = 0; i < size; i++) {
                if( key[i]!= -1) encodedBits[i] = new Qubit(base[i], key[i]);
                else encodedBits[i] = recivedQubits[i];
            }
        }
        return encodedBits;
    }

    @Override
    public String toString() {
        return "Person{" +
                "key=" + Arrays.toString(key) +
                ", base=" + Arrays.toString(base) +
                '}';
    }
}
