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

package com.asekulsk;

import com.asekulsk.nepichain.client.model.blockchain.Block;
import com.asekulsk.nepichain.client.model.blockchain.Chain;
import com.asekulsk.nepichain.client.model.blockchain.Message;
import com.asekulsk.nepichain.client.model.security.Mailbox;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

import java.security.PublicKey;
import java.security.Security;

/**
 * Example for Nepichain as messaging mail encryption or decryption blockchain.
 * - Blockchain can be validate for modifications
 * - Messages will be automatic ciphered from public key from BouncyCastle
 * - List from each mailbox has one blockchain
 *      - Jack Bauer
 *      - Max Mustersecurity
 *      - ...
 *
 * @author Andreas Sekulski
 *
 * <p>
 * Tutorial from...
 * https://medium.com/programmers-blockchain/create-simple-blockchain-java-tutorial-from-scratch-6eeed3cb03fa
 * https://medium.com/programmers-blockchain/creating-your-first-blockchain-with-java-part-2-transactions-2cdac335e0ce
 * </p>
 */
public class Main {

    public static void main(String[] args) {

        // Setup Bouncy castle as a Security Provider
        Security.addProvider(new BouncyCastleProvider());

        String textMessage;
        Mailbox mailbox_max_mustersecurity = new Mailbox("max_mustersecurity@com.de", "Max Mustersecurity");
        Mailbox mailbox_jack_bauer = new Mailbox("jack_bauer@24.de", "Jack Bauer");

        // Public key must be obtained from pki server to generate secured messages
        PublicKey jackPublicKey = mailbox_jack_bauer.getPublicKey();

        // Blockchain from Jack Bauer to create or obtain messages
        Chain chain_jack_bauer = mailbox_jack_bauer.getBlockchain();

        textMessage = "Hello Jack Bauer you got 24 hours to stop me ;)";
        Message message = mailbox_max_mustersecurity.createMessage(jackPublicKey, textMessage);
        chain_jack_bauer.addBlockToChain(new Block(message, chain_jack_bauer.getLastHash()));

        textMessage = "Tick tack... Jack Bauer you got 23 hours to stop me ;)";
        message = mailbox_max_mustersecurity.createMessage(jackPublicKey,textMessage);
        chain_jack_bauer.addBlockToChain(new Block(message, chain_jack_bauer.getLastHash()));

        textMessage = "Hello Jack the bomb is ticking ;)";
        message = mailbox_max_mustersecurity.createMessage(jackPublicKey, textMessage);
        chain_jack_bauer.addBlockToChain(new Block(message, chain_jack_bauer.getLastHash()));

        // Blockchain validation
        System.out.println(chain_jack_bauer.toJSON());
        System.out.println("Chain is " + (chain_jack_bauer.isChainValid() ? "valid" : "not valid"));
        System.out.println();

        // Decrypt messages for jack
        for (int i = 0; i < chain_jack_bauer.size(); i++) {
            System.out.println("Is signature verified : " + chain_jack_bauer.getMessage(i).verifySignature());
            System.out.println(mailbox_jack_bauer.getMessageFromBlock(chain_jack_bauer.getMessage(i)));
            System.out.println();
        }
    }
}
