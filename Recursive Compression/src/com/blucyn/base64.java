package com.blucyn;

import java.util.Base64;

public class base64 {
    //-------------------------------------------------------------------------------------------------------------- //
    // Constructor.

    // No param.
    public base64(){
        // Nothing needed.

    }


    //-------------------------------------------------------------------------------------------------------------- //
    // Encode/Decode methods.

    // Encodes a given string into base64.
    public String encode(String input){
        return Base64.getEncoder().withoutPadding().encodeToString(input.getBytes());
    }

    // Decodes a string from base64.
    public String decode(String input){
        byte[] decodedBytes = Base64.getUrlDecoder().decode(input);
        return new String(decodedBytes);
    }

}
