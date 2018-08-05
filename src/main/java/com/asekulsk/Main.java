package com.asekulsk;

import com.asekulsk.nepichain.client.model.blockchain.Block;
import com.asekulsk.nepichain.client.model.blockchain.Chain;
import com.asekulsk.nepichain.client.model.blockchain.Message;
import com.asekulsk.nepichain.client.model.security.Mailbox;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

import java.security.Security;

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

    public static void main(String[] args) {

        // Setup Bouncy castle as a Security Provider
        Security.addProvider(new BouncyCastleProvider());

        Mailbox mailbox_max_mustersecurity = new Mailbox("max_mustersecurity@com.de", "Max Mustersecurity");
        Mailbox mailbox_jack_bauer = new Mailbox("jack_bauer@24.de", "Jack Bauer");

        // Blockchain from Jack Bauer to obtain messages
        Chain chain_jack_bauer = new Chain(mailbox_jack_bauer.getFullName());

        Message message = mailbox_max_mustersecurity.createMessageForMailbox(mailbox_jack_bauer.getPublicKey(),
                "Hello Jack Bauer you got 24 hours to stop me ;)");
        chain_jack_bauer.addBlockToChain(new Block(message, chain_jack_bauer.getLastHash()));

        message = mailbox_max_mustersecurity.createMessageForMailbox(mailbox_jack_bauer.getPublicKey(),
                "Hello Jack Bauer you got 23 hours to stop me ;)");
        chain_jack_bauer.addBlockToChain(new Block(message, chain_jack_bauer.getLastHash()));

        message = mailbox_max_mustersecurity.createMessageForMailbox(mailbox_jack_bauer.getPublicKey(),
                "Hello Jack the bomb is ticking ;)");
        chain_jack_bauer.addBlockToChain(new Block(message, chain_jack_bauer.getLastHash()));

        // Blockchain validation
        System.out.println(chain_jack_bauer.toJSON());
        System.out.println("Chain is " + (chain_jack_bauer.isChainValid() ? "valid" : "not valid"));
        System.out.println();

        // Decrypt messages for jack
        for(int i = 0; i < chain_jack_bauer.size(); i++) {
            System.out.println("Is signature verified : " + chain_jack_bauer.getMessage(i).verifySignature());
            System.out.println(mailbox_jack_bauer.getMessageFromBlock(chain_jack_bauer.getMessage(i)));
            System.out.println();
        }

    }
}
