package com.blucyn;


public class Compressor {
    //-------------------------------------------------------------------------------------------------------------- //
    // Empty constructor.
    public Compressor(){}

    //-------------------------------------------------------------------------------------------------------------- //
    // Helper Methods.


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

    // Gets an arraylist of all unique pairs of letters within the input.
    public UniqueCounts getUniquePairs(String input){
        UniqueCounts u = new UniqueCounts();

        // Loop through all possible pairs of the string and process them.
        for(int i = 0; i < input.length() - 1; i++){
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

        return u;
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
    public String compressOnce(String input){
        // Get the most frequent pair within the input.
        String pair = this.getUniquePairs(input).getHighestPair();

        // Get the next available char.
        String letter = this.getOutsideChar(input);

        // Replace all occurrences of the letter with the pair.
        // Also add little instructions at the beginning for decompression.
        input = pair + letter + this.replaceWith(input, pair, letter);

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

    //-------------------------------------------------------------------------------------------------------------- //
    // More powerful compression methods.


    // Compresses a message over and over until the compression percent is under a 1% improvement.
    public String compress(String input) {
        String original = input;
        try {
            // Count keeps track of how many times input is compressed.
            int count = 0;

            // Loop forever.
            while (true) {
                String contender = this.compressOnce(input);

                // If contender is smaller than before, keep compressing.
                if (contender.length() < input.length()) {
                    input = contender;
                } else {
                    // If not, print stats then return the last compression.
                    this.printStats(original, input, count);

                    return count + " " + input;
                }

                // Print stats periodically.
                if(count % 5 == 0){
                    System.out.println("Gen " + count + " Improvement: " + this.getCompRate(original, input) + " %");
                }else{
                    System.out.println(".");
                }

                // Increment count.
                count++;
            }
        }catch (Exception e){
            return original;
        }
    }

    // Counter to compress.
    public String decompress(String input) {
        String original = input;
        try {
            // Assign proper values to setup variables.
            int splitIndex = input.indexOf(" ");
            int compTimes = Integer.parseInt(input.substring(0, splitIndex));
            input = input.substring(splitIndex + 1);

            // Decompress as many times as needed.
            for(int i = 0; i < compTimes; i++){
                input = this.decompressOnce(input);
            }

            return input;
        }catch (Exception e){
            System.out.println("ERROR");
            return original;
        }
    }
}
