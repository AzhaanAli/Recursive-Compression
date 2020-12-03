package com.blucyn;

import java.util.ArrayList;

// A method of storing two arrays of content in one object.
public class UniqueCounts {
    //--------------------------------------------------------------------------------------------------------------//

    // Instance Variables.
    ArrayList<String> pairs;
    ArrayList<Integer> counts;


    //--------------------------------------------------------------------------------------------------------------//

    // Constructor.
    public UniqueCounts(){
        this.pairs = new ArrayList<>();
        this.counts = new ArrayList<>();
    }

    //--------------------------------------------------------------------------------------------------------------//
    // Methods.


    // Prints the contents of the object with a second column being the times found.
    public void print(){
        System.out.println(" Pair" + "  " + "Count");
        System.out.println("-------------");
        for(int i = 0; i < this.pairs.size(); i++){
            System.out.println(" " + this.pairs.get(i) + "    " + this.counts.get(i));
        }
    }

    // Gets the pair with the highest count.
    public String getHighestPair(){
        int max = 0;
        int index = 0;

        // Loop through the whole array and find the highest index.
        for(int i = 0; i < this.counts.size(); i++){

            // If someone more powerful is found, assign new values.
            int contender = this.counts.get(i);
            if(contender > max){
                max = contender;
                index = i;
            }

        }

        return this.pairs.get(index);
    }
}