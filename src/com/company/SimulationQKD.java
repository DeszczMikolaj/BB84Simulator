package com.company;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class SimulationQKD {
    private static Strategy STRATEGY;
    public int[] alicesKeyRaw;
    public Qubit[] encryptedAliceKey;
    public int[] evasKeyRaw;
    public Qubit[]   encryptedEvasKey;
    public int[] bobsKeyRaw;
    public int size;
    public double aliceEveRawKeyIdentically;
    public double aliceBobRawKeyIdentically;
    public boolean[] indexesOfMatchingBases;
    public ArrayList<Integer> alicesKey;
    public ArrayList<Integer> bobsKey;
    public ArrayList<Integer> evasKey;
    public double aliceEveKeyIdentically;
    public double aliceBobKeyIdentically;
    private int detectorNoise;



    private double calculateIdentity(int[] key1, int[] key2){
        int identicalBits=0;
        for(int i=0; i<size;i++){
            if(key1[i]==key2[i]) identicalBits++;
        }
        return 100*identicalBits/size;
    }
    private double calculateIdentityArrayList( ArrayList<Integer> key1,  ArrayList<Integer> key2){
        int identicalBits=0;
        int size = key1.size();
        for(int i=0; i<size;i++){
            if(key1.get(i) == key2.get(i)) identicalBits++;
        }
        return 100*identicalBits/size;
    }
    private boolean[] getMatchingIndexes(String[] base1, String base2[]){
        boolean[] identicalBases = new boolean[size];
        for(int i=0; i<size;i++){
            if(base1[i]==base2[i]) identicalBases[i]=true;
            else identicalBases[i] = false ;
        }
        return identicalBases;
    }
    private ArrayList<Integer> getKey(int[] rawKey){
        ArrayList<Integer>  key = new ArrayList<>();
        for(int i=0; i<size;i++){
            if(indexesOfMatchingBases[i]==true) key.add(rawKey[i]);
        }
        return  key;
    }
    public void run(){
        System.out.println("BB84 QKD SIMULATOR:");
        System.out.println("Choose attack strategy:");
        System.out.println("1. All bits invaded ");
        System.out.println("2. Attack in series ");
        Scanner scanner = new Scanner(System.in);
        int choice = scanner.nextInt();
        switch (choice){
            case 1:{
                STRATEGY = Strategy.ALL;
                break;
            }
            case 2:{
                STRATEGY = Strategy.SERIES;
                break;
            }
        }
        System.out.println("Set detection noise [0-100]:");
        this.detectorNoise = scanner.nextInt();
        System.out.println("Set size of RawKey:");
        this.size = scanner.nextInt();
        Person Alice = new Person(size,detectorNoise,false);
        Person Eva = new Person(size,detectorNoise,true, STRATEGY);
        Person Bob = new Person(size,detectorNoise,false);

        Alice.key= Alice.getRandomNumbers(1,size);
        alicesKeyRaw = Alice.key;
        Alice.setBase();
        encryptedAliceKey = Alice.encode();
        System.out.println("Alice has generated random raw key and encoded it in random bases:");
        System.out.println("Alice: "+Alice.toString());
        Eva.setBase();
        evasKeyRaw = Eva.decode(encryptedAliceKey);
        System.out.println("Eva eavesdropped using "+ STRATEGY.getName()+" strategy, and decoded Alice's raw key in random bases:");
        System.out.println("Eva:  " + Eva.toString());
        encryptedEvasKey = Eva.encode();
        Bob.setBase();
        bobsKeyRaw = Bob.decode(encryptedEvasKey);
        System.out.println("Bob received from Eva corrupted raw key, and decoded it");
        System.out.println("Bob:  "+Bob.toString());
        aliceEveRawKeyIdentically = calculateIdentity(alicesKeyRaw, evasKeyRaw);
        aliceBobRawKeyIdentically = calculateIdentity(alicesKeyRaw, bobsKeyRaw);
        System.out.println("Eve has guessed "+ aliceEveRawKeyIdentically +"% of Alice's key");
        System.out.println("Bob has guessed "+ aliceBobRawKeyIdentically +"% of Alice's key");
        indexesOfMatchingBases = getMatchingIndexes(Alice.base,Bob.base);
        System.out.println("Establishing indexes of bits that was measured in the same bases: "+ Arrays.toString(indexesOfMatchingBases));
        alicesKey=getKey(alicesKeyRaw);
        bobsKey=getKey(bobsKeyRaw);
        evasKey=getKey(evasKeyRaw);
        System.out.println("Establishing keys from raw keys: ");
        System.out.println("Alice's key: "+ alicesKey.toString());
        System.out.println("Bob's key:  "+ bobsKey.toString());
        System.out.println("Eva's key:  "+evasKey.toString());
        aliceEveKeyIdentically = calculateIdentityArrayList(alicesKey , evasKey);
        aliceBobKeyIdentically = calculateIdentityArrayList(alicesKey, bobsKey);
        System.out.println("Eve posses "+ aliceEveKeyIdentically +"% of Alice's key");
        System.out.println("Bob posses "+ aliceBobKeyIdentically +"% of Alice's key");
        System.out.println("");
        System.out.println("Thanks for running this simulation. It may be upgraded with Key Distillation phase in future.");

    }
}
