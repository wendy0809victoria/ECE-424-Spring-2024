package main;
import info.blockchain.api.blockexplorer.*;
import info.blockchain.api.blockexplorer.entity.*;
import info.blockchain.api.*;
import java.lang.*;

import java.util.*;
import java.io.IOException;

import info.blockchain.api.blockexplorer.BlockExplorer;
import info.blockchain.api.APIException;
import info.blockchain.api.blockexplorer.entity.Output;

public class Checkpoint1 {

	/**
	 * Blocks-Q1: What is the size of this block?
	 *
	 * Hint: Use method getSize() in Block.java
	 *
	 * @return size of the block
	 */
	public long getBlockSize() {
		// TODO implement me
		try {
			BlockExplorer blkExp = new BlockExplorer();
			Block blk = blkExp.getBlock("000000000000000f5795bfe1de0381a44d4d5ea2ad81c21d77f275bffa03e8b3");
			return blk.getSize();
		} catch (APIException | IOException e) {
            e.printStackTrace();
        }
		return 0L;
	}

	/**
	 * Blocks-Q2: What is the Hash of the previous block?
	 *
	 * Hint: Use method getPreviousBlockHash() in Block.java
	 *
	 * @return hash of the previous block
	 */
	public String getPrevHash() {
		// TODO implement me
		try {
			BlockExplorer blkExp = new BlockExplorer();
			Block blk = blkExp.getBlock("000000000000000f5795bfe1de0381a44d4d5ea2ad81c21d77f275bffa03e8b3");
			return blk.getPreviousBlockHash();
		} catch (APIException | IOException e) {
            e.printStackTrace();
        }
		return "0";
	}

	/**
	 * Blocks-Q3: How many transactions are included in this block?
	 *
	 * Hint: To get a list of transactions in a block, use method
	 * getTransactions() in Block.java
	 *
	 * @return number of transactions in current block
	 */
	public int getTxCount() {
		// TODO implement me
		try {
			BlockExplorer blkExp = new BlockExplorer();
			Block blk = blkExp.getBlock("000000000000000f5795bfe1de0381a44d4d5ea2ad81c21d77f275bffa03e8b3");
			return blk.getTransactions().size();
		} catch (APIException | IOException e) {
            e.printStackTrace();
        }
		return 0;
	}

	/**
	 * Transactions-Q1: Find the transaction with the most outputs, and list the
	 * Bitcoin addresses of all the outputs.
	 *
	 * Hint: To get the bitcoin address of an Output object, use method
	 * getAddress() in Output.java
	 *
	 * @return list of output addresses
	 */
	public List<String> getOutputAddresses() {
		// TODO implement me
		try {
			BlockExplorer blkExp = new BlockExplorer();
			Block blk = blkExp.getBlock("000000000000000f5795bfe1de0381a44d4d5ea2ad81c21d77f275bffa03e8b3");
			List<Transaction> transactions = blk.getTransactions();
			List<String> addresses = new ArrayList<>();
			long maxCt = 0;
			for (Transaction transaction : transactions) {
				List<Output> outputs = transaction.getOutputs();
				if (outputs.size() > maxCt) {
					maxCt = outputs.size();
					addresses.clear();
					for (Output output : outputs) {
						String address = output.getAddress();
						addresses.add(address);
					}
				}
			}
			return addresses;
		} catch (APIException | IOException e) {
            e.printStackTrace();
        }
		return null;
	}

	/**
	 * Transactions-Q2: Find the transaction with the most inputs, and list the
	 * Bitcoin addresses of the previous outputs linked with these inputs.
	 *
	 * Hint: To get the previous output of an Input object, use method
	 * getPreviousOutput() in Input.java
	 *
	 * @return list of input addresses
	 */
	public List<String> getInputAddresses() {
		// TODO implement me
		try {
			BlockExplorer blkExp = new BlockExplorer();
			Block blk = blkExp.getBlock("000000000000000f5795bfe1de0381a44d4d5ea2ad81c21d77f275bffa03e8b3");
			List<Transaction> transactions = blk.getTransactions();
			List<String> addresses = new ArrayList<>();
			long maxCt = 0;
			for (Transaction transaction : transactions) {
				List<Input> inputs = transaction.getInputs();
				if (inputs.size() > maxCt) {
					maxCt = inputs.size();
					addresses.clear();
					for (Input input : inputs) {
						String address = input.getPreviousOutput().getAddress();
						addresses.add(address);
					}
				}
			}
			return addresses;
		} catch (APIException | IOException e) {
            e.printStackTrace();
        }
		return null;
	}

	/**
	 * Transactions-Q3: What is the bitcoin address that has received the
	 * largest amount of Satoshi in a single transaction?
	 *
	 * Hint: To get the number of Satoshi received by an Output object, use
	 * method getValue() in Output.java
	 *
	 * @return the bitcoin address that has received the largest amount of Satoshi
	 */
	public String getLargestRcv() {
		// TODO implement me
		try {
			BlockExplorer blkExp = new BlockExplorer();
			Block blk = blkExp.getBlock("000000000000000f5795bfe1de0381a44d4d5ea2ad81c21d77f275bffa03e8b3");
			List<Transaction> transactions = blk.getTransactions();
			Map<String, Integer> valCounts = new HashMap<>();
			long curMax = 0;
			String curAddr = "0";
			for (Transaction transaction : transactions) {
				List<Output> outputs = transaction.getOutputs();
				for (Output output : outputs) {
					if (curMax < output.getValue()) {
						curMax = output.getValue();
						curAddr = output.getAddress();
					}
				}
			}
			return curAddr;
		} catch (APIException | IOException e) {
            e.printStackTrace();
        }
		return "0";
	}

	/**
	 * Transactions-Q4: How many coinbase transactions are there in this block?
	 *
	 * Hint: In a coinbase transaction, getPreviousOutput() == null --> although this matches with the documentation, the result is wrong.
	 * Even if it's a coinbase transactions, it's not null during my test.
	 * I would recommend another work around that a coinbase transaction should have the sum of getPreviousOutput().getValue() equal to 0 because the total input should be 0.
	 * You can see an example of coinbase transaction here: https://www.blockchain.com/btc/tx/cdab676fe718b5155251f15b275c5f92ad965ee8557270d1eec07ccc42d4aaaf
	 * I'm using Java 1.8.0_242, if anyone made it success with getPreviousOutput() == null, please email me or send a campuswire post. Much appreciated!
	 *
	 * @return number of coin base transactions
	 */
	public int getCoinbaseCount() {
		// TODO implement me
		try {
			BlockExplorer blkExp = new BlockExplorer();
			Block blk = blkExp.getBlock("000000000000000f5795bfe1de0381a44d4d5ea2ad81c21d77f275bffa03e8b3");
			List<Transaction> transactions = blk.getTransactions();
			int zeroCt = 0;
			long sumVal = 0;
			for (Transaction transaction : transactions) {
				List<Input> inputs = transaction.getInputs();
				sumVal = 0;
				for (Input input : inputs) {
					sumVal = sumVal + input.getPreviousOutput().getValue();
				}
				if (sumVal == 0) {
					zeroCt = zeroCt + 1;
				}
			}
			return zeroCt;
		} catch (APIException | IOException e) {
            e.printStackTrace();
        }
		return 0;
	}

	/**
	 * Transactions-Q5: What is the number of Satoshi generated in this block?
	 *
	 * @return number of Satoshi generated
	 */
	public long getSatoshiGen() {
		// TODO implement me
		try {
			BlockExplorer blkExp = new BlockExplorer();
			Block blk = blkExp.getBlock("000000000000000f5795bfe1de0381a44d4d5ea2ad81c21d77f275bffa03e8b3");
			List<Transaction> transactions = blk.getTransactions();
			int zeroCt = 0;
			long sumVal = 0;
			long sumGen = 0;
			for (Transaction transaction : transactions) {
				List<Input> inputs = transaction.getInputs();
				sumVal = 0;
				for (Input input : inputs) {
					sumVal = sumVal + input.getPreviousOutput().getValue();
				}
				if (sumVal == 0) {
					zeroCt = zeroCt + 1;
					List<Output> outputs = transaction.getOutputs();
					sumGen = 0;
					for (Output output : outputs) {
						sumGen = sumGen + output.getValue();
					}
				}
			}
			return sumGen;
		} catch (APIException | IOException e) {
            e.printStackTrace();
        }
		return 0L;
	}

}
