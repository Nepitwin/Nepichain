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

package com.asekulsk.nepichain.client.model.blockchain;

import com.google.gson.Gson;

import java.util.ArrayList;

/**
 * Chain class to store all Blocks from messaging.
 *
 * @author Andreas Sekulski
 */
public class Chain {

    /**
     * Blockchain to store block messages to user.
     */
    private ArrayList<Block> blockchain;

    /**
     * Owner from chain to send messages.
     */
    private String owner;

    /**
     * GSON to serialize json data.
     */
    private Gson gson;

    /**
     * Constructor to create blockchain for a given owner.
     *
     * @param owner Owner from blockchain.
     */
    public Chain(String owner) {
        this.blockchain = new ArrayList<>();
        this.gson = new Gson();
        this.owner = owner;
    }

    /**
     * Add block to chain.
     *
     * @param message Crypted message to add in chain.
     * @return TRUE if block is added otherwise FALSE.
     */
    public boolean addMessageToChain(Message message) {
        return message != null && blockchain.add(new Block(message, getLastHash()));
    }

    /**
     * Generates blockchain as json.
     *
     * @return JSON string from blockchain.
     */
    public String toJSON() {
        return gson.toJson(blockchain);
    }

    /**
     * Get message from blockchain.
     *
     * @param index Index from message to obtain.
     * @return NULL if index greater or smaller than blockchain size otherwise message.
     */
    public Message getMessage(int index) {
        return index < size() && index >= 0 ? blockchain.get(index).getData() : null;
    }

    /**
     * Size from blockchain.
     *
     * @return Size from blockchain as integer.
     */
    public int size() {
        return blockchain.size();
    }

    /**
     * Verification if chain still valid and not manipulated.
     *
     * @return TRUE if chain is valid otherwise false.
     */
    public Boolean isChainValid() {

        Block currentBlock;
        Block previousBlock;

        //loop through blockchain to check hashes:
        for (int i = 1; i < blockchain.size(); i++) {
            currentBlock = blockchain.get(i);
            previousBlock = blockchain.get(i - 1);

            // compare registered hash and calculated hash:
            // Check hash from next block
            if (!currentBlock.getHash().equals(currentBlock.calculateHash())) {
                System.out.println("Current Hashes not equal");
                return false;
            }

            //compare previous hash and registered previous hash
            if (!previousBlock.getHash().equals(currentBlock.getPreviousHash())) {
                System.out.println("Previous Hashes not equal");
                return false;
            }

            // Andy FIX := I modified first mail and chain validation will be true if not checked first chain element because algorithm don't check hash calculation from first element.
            // For immutable model classes not needed.
            //check if previous block not modified.
            if (!previousBlock.getHash().equals(previousBlock.calculateHash())) {
                System.out.println("Hash block modified");
                return false;
            }
        }
        return true;
    }

    /**
     * Get last hash value from blockchain.
     *
     * @return If blockchain is empty "0" will be returned otherwise actual hash from last block.
     */
    private String getLastHash() {
        return blockchain.isEmpty() ? "0" : blockchain.get(blockchain.size() - 1).getHash();
    }
}
