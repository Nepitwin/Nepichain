/*
 * Copyright 2018 Andreas Sekulski
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.asekulsk.nepichain.util;

import org.bouncycastle.jce.provider.BouncyCastleProvider;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.security.*;

/**
 * Bouncy castle crypto helper class.
 *
 * @author Andreas Sekulski
 */
public class Crypto {

    /**
     * Encrypt plaintext message with public key.
     *
     * @param key       Public key to encrypt.
     * @param plaintext Plain text to encryt.
     * @return Ciphered message as byte array.
     * @throws NoSuchPaddingException    This exception is thrown when a particular padding mechanism is requested but is not available in the environment.
     * @throws NoSuchAlgorithmException  This exception is thrown when a particular cryptographic algorithm is requested but is not available in the environment.
     * @throws NoSuchProviderException   This exception is thrown when a particular security provider is requested but is not available in the environment.
     * @throws InvalidKeyException       This is the exception for invalid Keys (invalid encoding, wrong length, uninitialized, etc).
     * @throws BadPaddingException       This exception is thrown when a particular padding mechanism is expected for the input data but the data is not padded properly.
     * @throws IllegalBlockSizeException This exception is thrown when the length of data provided to a block cipher is incorrect, i.e., does not match the block size of the cipher.
     */
    public static byte[] encrypt(PublicKey key, String plaintext) throws NoSuchPaddingException, NoSuchAlgorithmException, NoSuchProviderException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException {
        Cipher cipher = Cipher.getInstance("ECIES", BouncyCastleProvider.PROVIDER_NAME);
        cipher.init(Cipher.ENCRYPT_MODE, key);
        return cipher.doFinal(plaintext.getBytes());
    }

    /**
     * Decipher data with private key.
     *
     * @param key  Private key for decipher usage.
     * @param data Data to decipher.
     * @return Ciphered data as byte array.
     * @throws NoSuchPaddingException    This exception is thrown when a particular padding mechanism is requested but is not available in the environment.
     * @throws NoSuchAlgorithmException  This exception is thrown when a particular cryptographic algorithm is requested but is not available in the environment.
     * @throws NoSuchProviderException   This exception is thrown when a particular security provider is requested but is not available in the environment.
     * @throws InvalidKeyException       This is the exception for invalid Keys (invalid encoding, wrong length, uninitialized, etc).
     * @throws BadPaddingException       This exception is thrown when a particular padding mechanism is expected for the input data but the data is not padded properly.
     * @throws IllegalBlockSizeException This exception is thrown when the length of data provided to a block cipher is incorrect, i.e., does not match the block size of the cipher.
     */
    public static byte[] decipher(PrivateKey key, byte[] data) throws NoSuchPaddingException, NoSuchAlgorithmException, NoSuchProviderException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException {
        Cipher decipher = Cipher.getInstance("ECIES", BouncyCastleProvider.PROVIDER_NAME);
        decipher.init(Cipher.DECRYPT_MODE, key);
        return decipher.doFinal(data);
    }

    /**
     * Applies SHA256 to a string and returns the result.
     *
     * @param input Input to generate as SHA256
     * @return SHA256 String output.
     */
    public static String SHA256(String input) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            //Applies sha256 to our input,
            byte[] hash = digest.digest(input.getBytes("UTF-8"));
            StringBuffer hexString = new StringBuffer(); // This will contain hash as hexidecimal
            for (int i = 0; i < hash.length; i++) {
                String hex = Integer.toHexString(0xff & hash[i]);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Applies ECDSA Signature and returns the result ( as bytes ).
     *
     * @param privateKey Private key to generate signature.
     * @param input      Input to generate signature.
     * @return ECDSA Signature as bytes.
     */
    public static byte[] generateECDSASig(PrivateKey privateKey, String input) {
        Signature dsa;
        byte[] output = new byte[0];
        try {
            dsa = Signature.getInstance("ECDSA", "BC");
            dsa.initSign(privateKey);
            byte[] strByte = input.getBytes();
            dsa.update(strByte);
            byte[] realSig = dsa.sign();
            output = realSig;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return output;
    }

    /**
     * Verifies a String signature
     *
     * @param publicKey Public key to generate signature.
     * @param data      Date to verify.
     * @param signature Signature to check.
     * @return TRUE if signature is valid FALSE if not.
     */
    public static boolean verifyECDSASig(PublicKey publicKey, String data, byte[] signature) {
        try {
            Signature ecdsaVerify = Signature.getInstance("ECDSA", "BC");
            ecdsaVerify.initVerify(publicKey);
            ecdsaVerify.update(data.getBytes());
            return ecdsaVerify.verify(signature);
        } catch (Exception e) {
            return false;
            //throw new RuntimeException(e);
        }
    }
}
