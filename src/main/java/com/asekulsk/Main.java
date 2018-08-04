package com.asekulsk;

import com.asekulsk.nepichain.client.model.blockchain.Block;
import com.asekulsk.nepichain.client.model.security.Mailbox;
import com.google.gson.GsonBuilder;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

import java.security.Security;
import java.util.ArrayList;

/**
 * Example for Nepichain as messaging mail encryption or decryption service.
 *   - Blockchain can be validate for modifications
 *   - Messages will be automatic ciphered from public key from BouncyCastle
 *   - List from each mailbox has one blockchain
 *     - Jack Bauer
 *     - Max Mustersecurity
 *     - ...
 *
 * @author Andreas Sekulski
 *
 * https://medium.com/programmers-blockchain/create-simple-blockchain-java-tutorial-from-scratch-6eeed3cb03fa
 * https://medium.com/programmers-blockchain/creating-your-first-blockchain-with-java-part-2-transactions-2cdac335e0ce
 */
public class Main {

    /**
     * Jack Bauers Mailbox blockchain
     */
    public static ArrayList<Block> blockchain_jack_bauer = new ArrayList<Block>();

    public static ArrayList<Block> blockchain_max_mustersecurity = new ArrayList<Block>();

    public static void main(String[] args) {
        //Setup Bouncy castle as a Security Provider
        Security.addProvider(new BouncyCastleProvider());

        Mailbox mailbox_max_mustersecurity = new Mailbox("max_mustersecurity@com.de", "Max Mustersecurity");
        Mailbox mailbox_jack_bauer = new Mailbox("jack_bauer@24.de", "Jack Bauer");

        // ToDo := This could be nicer to make a java class blockchain...
        // Send e-mail messages to jack ;)
        blockchain_jack_bauer.add(
                new Block(
                        mailbox_max_mustersecurity.createMessageForMailbox(mailbox_jack_bauer.getPublicKey(),
                                "Hello Jack Bauer you got 24 hours to stop me ;)"),
                        "0"));

        blockchain_jack_bauer.add(
                new Block(
                        mailbox_max_mustersecurity.createMessageForMailbox(mailbox_jack_bauer.getPublicKey(),
                                "Hello Jack Bauer you got 23 hours to stop me ;)"),
                        blockchain_jack_bauer.get(blockchain_jack_bauer.size()-1).getHash()));

        blockchain_jack_bauer.add(
                new Block(
                        mailbox_max_mustersecurity.createMessageForMailbox(mailbox_jack_bauer.getPublicKey(),
                                "Hello Jack the bomb is ticking ;)"),
                        blockchain_jack_bauer.get(blockchain_jack_bauer.size()-1).getHash()));

        // Blockchain validation
        String blockchainJson = new GsonBuilder().setPrettyPrinting().create().toJson(blockchain_jack_bauer);
        System.out.println(blockchainJson);
        System.out.println("Chain is " + (isChainValid() ? "valid" : "not valid"));
        System.out.println();

        //Verify the signature works and verify it from the public key
        System.out.println("Is signature verified :" + blockchain_jack_bauer.get(0).getData().verifySignature());
        System.out.println();

        // Decrypt messages for jack
        System.out.println(mailbox_jack_bauer.getMessageFromBlock(blockchain_jack_bauer.get(0).getData()));
        System.out.println(mailbox_jack_bauer.getMessageFromBlock(blockchain_jack_bauer.get(1).getData()));
        System.out.println(mailbox_jack_bauer.getMessageFromBlock(blockchain_jack_bauer.get(2).getData()));
        System.out.println();
    }

    public static Boolean isChainValid() {
        Block currentBlock;
        Block previousBlock;

        //loop through blockchain to check hashes:
        for(int i=1; i < blockchain_jack_bauer.size(); i++) {
            currentBlock = blockchain_jack_bauer.get(i);
            previousBlock = blockchain_jack_bauer.get(i-1);

            // compare registered hash and calculated hash:
            // Check hash from next block
            if(!currentBlock.getHash().equals(currentBlock.calculateHash()) ){
                System.out.println("Current Hashes not equal");
                return false;
            }

            //compare previous hash and registered previous hash
            if(!previousBlock.getHash().equals(currentBlock.getPreviousHash()) ) {
                System.out.println("Previous Hashes not equal");
                return false;
            }

            // ToDo := Andy FIX := I modified first mail and chain validation will be true if not checked first chain element because algorithm don't check hash calculation from first element
            //check if previous block not modified
            if(!previousBlock.getHash().equals(previousBlock.calculateHash()) ) {
                System.out.println("Hash block modified");
                return false;
            }
        }
        return true;
    }
}
