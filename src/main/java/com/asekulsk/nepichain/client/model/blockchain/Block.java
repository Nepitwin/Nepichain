package com.asekulsk.nepichain.client.model.blockchain;

import com.asekulsk.nepichain.util.Crypto;

import java.util.Date;

/**
 * Immutable block to store data for blockchain.
 * @author Andreas Sekulski
 */
public class Block {

    /**
     * Hash to verify if data from block is valid.
     */
    private String hash;

    /**
     * Previous hash block for block chain verification. If block is first element hash ist 0
     */
    private String previousHash;

    /**
     * Message data to store for communication.
     */
    private Message data;

    /**
     * Timestamp from block generation as milliseconds since 1/1/1970.
     */
    private long timeStamp;

    /**
     * Block constructor to create a block for a message.
     * @param data Message to store for blockchain.
     * @param previousHash Previous hash from block to store.
     */
    public Block(Message data, String previousHash) {
        this.data = data;
        this.previousHash = previousHash;
        this.timeStamp = new Date().getTime();
        this.hash = calculateHash(); //Making sure we do this after we set the other values.
    }

    /**
     * Get hash signature from block to verify integrity.
     * @return SHA256 hash to verify.
     */
    public String getHash() {
        return hash;
    }

    /**
     * Get hash from previous block element if not first.
     * @return If not first element SHA256 will be returned otherwise 0.
     */
    public String getPreviousHash() {
        return previousHash;
    }

    /**
     * Get deciphered message data from block.
     * @return Deciphered message data which only receiver can decipher.
     */
    public Message getData() {
        return data;
    }

    /**
     * Get timestamp from block creation.
     * @return Timestamp in ms since 1/1/1970.
     */
    public long getTimeStamp() {
        return timeStamp;
    }

    /**
     * Calculate hash from block to check for manipulation.
     * @return SHA256 hash from block.
     */
    public String calculateHash() {
        return Crypto.SHA256(
                previousHash + Long.toString(timeStamp) + data.calculateHash()
        );
    }
}
