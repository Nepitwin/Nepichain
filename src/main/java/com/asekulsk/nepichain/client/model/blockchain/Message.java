package com.asekulsk.nepichain.client.model.blockchain;

import com.asekulsk.nepichain.util.Crypto;
import com.asekulsk.nepichain.util.StringUtil;

import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Arrays;

/**
 * Immutable message block to store e-mail messages.
 * @author Andreas Sekulski
 */
public class Message {

    /**
     * Public key from sender to verify.
     */
    private PublicKey sender;

    /**
     * Public key from recipient to obtain message.
     */
    private PublicKey recipient;

    /**
     * Encrypted message for recipient.
     */
    private byte[] message;

    /**
     * Signature from message to verify data for manipulation.
     */
    private byte[] signature;

    /**
     * Creates message for given user.
     * @param signatureKey Signature key to create signature hash. Will not be stored!!!
     * @param from Public key sender to verify signature.
     * @param to Public key reciepient to decrypt data.
     * @param message Message to cipher.
     */
    public Message(PrivateKey signatureKey, PublicKey from, PublicKey to, String message) {
        this.sender = from;
        this.recipient = to;
        try {
            this.message = Crypto.encrypt(to, message);
        } catch (Exception e) {
            e.printStackTrace();
        }
        generateSignature(signatureKey);
    }

    /**
     * Get public key from sender.
     * @return Sender public key.
     */
    public PublicKey getSender() {
        return sender;
    }

    /**
     * Get public key from recipient.
     * @return Public key from recipient to check.
     */
    public PublicKey getReciepient() {
        return recipient;
    }

    /**
     * Ciphered message as byte array which only recipient can decipher.
     * @return Get ciphered message as byte array.
     */
    public byte[] getMessage() {
        return message;
    }

    /**
     * Get signature from message to verify for manipulation.
     * @return Signature to check if message is valid.
     */
    public byte[] getSignature() {
        return signature;
    }

    /**
     * Calculates hash from message.
     * @return Hash to verify if data is not modified.
     */
    public String calculateHash() {
        return Crypto.SHA256(
                StringUtil.getStringFromKey(sender) + StringUtil.getStringFromKey(recipient) + message + Arrays.toString(signature)
        );
    }

    /**
     * Verifies the data we signed has not been tampered with
     * @return TRUE if signature is valid otherwise FALSE.
     */
    public boolean verifySignature() {
        String data = StringUtil.getStringFromKey(sender) + StringUtil.getStringFromKey(recipient) + message;
        return Crypto.verifyECDSASig(sender, data, signature);
    }

    /**
     * Signs all the data we dont wish to be tampered with.
     * @param privateKey Private key to generate signature.
     */
    private void generateSignature(PrivateKey privateKey) {
        String data = StringUtil.getStringFromKey(sender) + StringUtil.getStringFromKey(recipient) + message;
        signature = Crypto.generateECDSASig(privateKey,data);
    }
}
