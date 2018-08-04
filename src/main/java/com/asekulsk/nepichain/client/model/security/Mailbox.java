package com.asekulsk.nepichain.client.model.security;

import com.asekulsk.nepichain.client.model.blockchain.Block;
import com.asekulsk.nepichain.client.model.blockchain.Message;
import com.asekulsk.nepichain.util.Crypto;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.security.*;
import java.security.spec.ECGenParameterSpec;
import java.util.ArrayList;

/**
 * Mailbox to store client data from a mailbox for example private or public keys.
 * @author Andreas Sekulski
 */
public class Mailbox {

    /**
     * Public key from mailbox to verify data.
     */
    private PublicKey publicKey;

    /**
     * Private key from mail box which is should be stored safely.
     */
    private PrivateKey privateKey;

    /**
     * E-Mail from mailbox to store data.
     */
    private String email;

    /**
     * Full name from user which mailbox contains.
     */
    private String fullName;

    /**
     * Blockchain to store message blocks for each mailbox.
     */
    public ArrayList<Block> blockchain;

    /**
     * Constructor to create mailbox for a specific user and email.
     * @param email E-Mail from user.
     * @param fullName Full name from user for example Max Mustermann.
     */
    public Mailbox(String email, String fullName){
        generateKeyPair();
        this.email = email;
        this.fullName = fullName;
        this.blockchain = new ArrayList<>();
    }

    /**
     * Get public key from mailbox user.
     * @return Public key for crypto usage.
     */
    public PublicKey getPublicKey() {
        return publicKey;
    }

    /**
     * Get e-mail from mailbox which is used.
     * @return E-Mail from user.
     */
    public String getEmail() {
        return email;
    }

    /**
     * Get full name from mailbox to use.
     * @return Full name as String for example Max Mustermann.
     */
    public String getFullName() {
        return fullName;
    }

    /**
     * Generates secured message for blockchain usage to send.
     * @param receiverKey Public key from receiver.
     * @param message Message to send.
     */
    public Message createMessageForMailbox(PublicKey receiverKey, String message) {
        return new Message(privateKey, publicKey, receiverKey, message);
    }

    /**
     * Decipher message from given block message.
     * @param message Message to decipher.
     * @return Empty String if not successfully otherwise deciphered message.
     */
    public String getMessageFromBlock(Message message) {

        byte[] byte_message = {};

        try {
            byte_message = Crypto.decipher(privateKey, message.getMessage());
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchProviderException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        }

        return new String(byte_message);
    }

    /**
     * Generate ECC key pair for secured messaging.
     */
    private void generateKeyPair() {
        try {
            KeyPairGenerator keyGen = KeyPairGenerator.getInstance("EC", BouncyCastleProvider.PROVIDER_NAME);
            ECGenParameterSpec ecSpec = new ECGenParameterSpec("prime256v1");
            SecureRandom random = new SecureRandom();

            // Initialize the key generator and generate a KeyPair
            keyGen.initialize(ecSpec, random);
            KeyPair keyPair = keyGen.generateKeyPair();

            // Set the public and private keys from the keyPair
            privateKey = keyPair.getPrivate();
            publicKey = keyPair.getPublic();
        }catch(Exception e) {
            throw new RuntimeException(e);
        }
    }
}
