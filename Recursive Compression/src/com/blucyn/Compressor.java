package com.blucyn;


import java.util.*;

public class Compressor {
    //-------------------------------------------------------------------------------------------------------------- //
    // Empty constructor.
    public Compressor(){}

    //-------------------------------------------------------------------------------------------------------------- //
    // Empty constructor.


    // Replaces every occurrence of a substring within the string to another.
    public String replaceWith(String input, String searchFor, String assignAs){
        // Escape early if this will go on forever without end.
        if(assignAs.contains(searchFor)){
            return input;
        }

        // While input contains search word, replace it with its associated term.
        while(input.contains(searchFor)){
            input = input.replace(searchFor, assignAs);
        }

        return input;
    }

    // Gets a character that is NOT in the input.
    public String getOutsideChar(String input){
        try {
            // Loop through all possible character values.
            int count = 32;

            // If its within the input, just try the next one.
            while (input.contains(String.valueOf((char)count))) {
                count++;
            }

            // Return the first one found that isn't in input.
            return String.valueOf((char)count);

        }catch (Exception e){
            // Lazy error checking.
            return "error";
        }
    }
    public String[] getOutsideChar(String input, int amount){
        try {

            String[] characters = new String[amount];

            // Loop through all possible character values.
            int count = 32;

            for(int i = 0; i < amount; i++){
                // If its within the input, just try the next one.
                while (input.contains(String.valueOf((char)count))) {
                    count++;
                }

                // Return the first one found that isn't in input.
                input += (char)count;
                characters[i] = String.valueOf((char)count);
            }

            return characters;

        }catch (Exception e){
            // Lazy error checking.
            System.out.println("ERROR IN GET UNIQUE CHARS METHOD");
            return null;
        }
    }

    // Gets an arraylist of all unique pairs of letters within the input.
    public ArrayList<String> getUniquePairs(String input){
        UniqueCounts u = new UniqueCounts();

        // Loop through all possible pairs of the string and process them.
        for(int i = 1; i < input.length() - 1; i++){
            String pair = input.substring(i, i + 2);

            // If it's already in the arraylist, then increment counts.
            if (u.pairs.contains(pair)){
                int index = u.pairs.indexOf(pair);
                int num = u.counts.get(index);

                u.counts.set(index, num + 1);
            }else{
                // Otherwise, append it to unique pairs.
                u.pairs.add(pair);
                u.counts.add(1);
            }
        }

        // Sort pairs array.
        final List<String> stringListCopy = u.pairs;
        ArrayList<String> sortedList = new ArrayList(stringListCopy);
        sortedList.sort(Comparator.comparingInt(s -> u.counts.get(stringListCopy.indexOf(s))));

        return sortedList;
    }
    public ArrayList<String> getSortedWords(String input){
        UniqueCounts u = new UniqueCounts();

        // Loop through all possible pairs of the string and process them.
        for(String word : input.split(" ")){

            // If it's already in the arraylist, then increment counts.
            if (u.pairs.contains(word)){
                int index = u.pairs.indexOf(word);
                int num = u.counts.get(index);

                u.counts.set(index, num + 1);
            }else{
                // Otherwise, append it to unique pairs.
                u.pairs.add(word);
                u.counts.add(1);
            }
        }

        // Sort pairs array.
        final List<String> stringListCopy = u.pairs;
        ArrayList<String> sortedList = new ArrayList(stringListCopy);
        sortedList.sort(Comparator.comparingInt(s -> u.counts.get(stringListCopy.indexOf(s))));

        return sortedList;
    }

    // Gets the rate of compression.
    public double getCompRate(String original, String compressed){

        return Math.round((1 - (double)compressed.length() / original.length()) * 10000) / 100.0;
    }

    // Prints the stats at the end of the compression cycle.
    private void printStats(String original, String input, int count){
        // Initialize strings.
        String border = "+=----------------------------------------------------=+";
        String gen = "Final Generation " + count;
        String improved = "Final Improvement: " + this.getCompRate(original, input) + " %";

        int genHalfLen = gen.length() / 2;
        int impHalfLen = improved.length() / 2;


        // Correct to make centered.
        while(gen.length() < border.length() / 2 + genHalfLen){
            gen = " " + gen;
        }
        while(improved.length() < border.length() / 2 + impHalfLen){
            improved = " " + improved;
        }

        // Print end result.
        System.out.println(border);
        System.out.println(gen);
        System.out.println(improved);
        System.out.println(border);
    }

    //-------------------------------------------------------------------------------------------------------------- //
    // Normal compression methods.


    // Compresses a string based on its most frequent pairs.
    public String compressOnce(String input, int strength){
        // Get the most frequent pairs within the input.
        ArrayList<String> pairs = this.getUniquePairs(input);

        if(pairs.size() < strength){
            System.out.println("Strength is too high for given text. Maximum strength is " + pairs.size() + ".");
            return null;
        }

        // Get the next [amount] available chars.
        String[] letters = this.getOutsideChar(input, strength);

        for(int i = 0; i < strength; i++){
            String pair = pairs.get(pairs.size() - 1 - i);
            String letter = letters[i];

            // Replace all occurrences of the letter with the pair.
            // Also add little instructions at the beginning for decompression.
            input = pair + letter + this.replaceWith(input, pair, letter);
        }

        return input;
    }

    // Decompresses a string.
    public String decompressOnce(String input){
        // Get searchFor.
        String searchFor = input.substring(2, 3);

        // Get assignAs.
        String assignAs = input.substring(0, 2);

        // Cut instructions out of input.
        input = input.substring(3);

        // Replace all occurrences of the pair with the letter.
        input = this.replaceWith(input, searchFor, assignAs);


        return input;
    }
    public String decompressOnce(String input, int amount){

        for(int i = 0; i < amount; i++){
            input = this.decompressOnce(input);
        }
        return input;

    }


    //-------------------------------------------------------------------------------------------------------------- //
    // More powerful compression methods.

    // Compresses a message over and over.

    public String compress(String input){

        ArrayList<String> pairs = this.getUniquePairs(input);
        return this.compress(
                input,
                -1,
                Math.min(20, pairs.size()),
                false
        );

    }
    public String compress(String input, int times){

        ArrayList<String> pairs = this.getUniquePairs(input);
        return this.compress(
                input,
                times,
                Math.min(20, pairs.size()),
                false
        );

    }
    public String compress(String input, int times, int strength){

        return this.compress(input, times, strength, false);

    }
    public String compress(String input, int times, int strength, boolean hideImprovement){

        // If times is -1, then go as long as you can improve.
        if (times == -1) {
            times = Integer.MAX_VALUE - 1;
        }

        // Initialize a reference point.
        String original = input;

        // Take away all enters and replace with a character that isn't in input.
        String letter = this.getOutsideChar(input);
        input = letter + this.replaceWith(input, "\n", letter);

        // Set up a counter to keep track of how many iterations it's been.
        int count = 0;

        // Loop either until compression is hurting the size of the String, or count is greater than the times parameter.
        while(true){

            // Return if count is greater than times.
            if(count > times){
                System.out.println("Gen " + count + " Improvement: " + this.getCompRate(original, input) + " %");
                return strength + " " + count + " " + input;
            }

            // Compress once.
            String next = this.compressOnce(input, strength);

            // If that helped, keep going. If it did not, then return early.
            if(next.length() >= input.length()){
                input = strength + " " + count + " " + input;
                System.out.println("Final Gen " + count + " Improvement: " + this.getCompRate(original, input) + " %");
                return input;
            }else{

                // Assign new value to input.
                input = next;

                // If hide improvement is off, then print improvement records.
                if(!hideImprovement){
                    if(count % 5 == 0){
                        System.out.println("Gen " + count + " Improvement: " + this.getCompRate(original, input) + " %");
                    }
                    System.out.println(".");
                }
                count++;
            }
        }

    }

    public String compressCostly(String input){

        return this.compress(
                input,
                -1,
                1,
                false
        );

    }

    // Counter to compress.
    public String decompress(String input) {
        String original = input;
        try {
            // Assign proper values to setup variables.
            int splitIndex = input.indexOf(" ");
            int strength = Integer.parseInt(input.substring(0, splitIndex));
            input = input.substring(splitIndex + 1);

            splitIndex = input.indexOf(" ");
            int compTimes = Integer.parseInt(input.substring(0, splitIndex));
            input = input.substring(splitIndex + 1);

            // Decompress as many times as needed.
            for(int i = 0; i < compTimes; i++){
                input = this.decompressOnce(input, strength);
            }

            input = this.replaceWith(input,
                    input.substring(0,1),
                    "\n"
            );
            return input.substring(1);

        }catch (Exception e){
            System.out.println("ERROR WHEN DECOMPRESSING");
            return original;
        }
    }

}