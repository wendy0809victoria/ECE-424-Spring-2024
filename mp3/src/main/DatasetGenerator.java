package main;
import info.blockchain.api.blockexplorer.*;
import info.blockchain.api.blockexplorer.entity.*;
import info.blockchain.api.*;
import java.lang.*;

import java.util.*;
import java.io.FileWriter;
import java.io.IOException;

import java.io.BufferedWriter;
import java.util.List;


public class DatasetGenerator {
	String file;

	public DatasetGenerator(String file) {
		this.file = file;
	}

	public boolean writeTransactions() {
		// TODO implement me
		BlockExplorer blkExp = new BlockExplorer();
		long height;
		String address;
		String txHash;
		String or;
		String ir;
		long val;
		long sumVal;
		try {
			FileWriter writer = new FileWriter(file);
			try {
				for (height = 265852; height < 266086; height++) {
					List<Block> blocks = blkExp.getBlocksAtHeight(height);
					for (Block blk : blocks) {
						List<Transaction> transactions = blk.getTransactions();
						for (Transaction transaction : transactions) {
							List<Input> inputs = transaction.getInputs();
							sumVal = 0;
							for (Input input : inputs) {
								sumVal = 0;
								sumVal = sumVal + input.getPreviousOutput().getValue();
							}
								
							if (sumVal != 0) {
								txHash = transaction.getHash();
								List<Output> outputs = transaction.getOutputs();
								for (Output output : outputs) {
									address = output.getAddress();
									val = output.getValue();
									or = generateOutputRecord(txHash, address, val);
									if (address != "") {
										writer.write(or+"\n");
									}
								}
								for (Input input : inputs) {
									val = input.getPreviousOutput().getValue();
									address = input.getPreviousOutput().getAddress();
									ir = generateInputRecord(txHash, address, val);
									if (address != "") {
										writer.write(ir+"\n");
									}
								}
							}
						}
					}
				}
				writer.close();
				return true;
			} catch (APIException | IOException e1) {
				e1.printStackTrace();
			}
		} catch (IOException e2) {
			e2.printStackTrace();
		}
    	return false;
	}

	/**
	 * Generate a record in the transaction dataset
	 *
	 * @param txHash
	 *            Transaction hash
	 * @param address
	 *            Previous output address of the input
	 * @param value
	 *            Number of Satoshi transferred
	 * @return A record of the input
	 */
	private String generateInputRecord(String txHash,
			String address, long value) {
		return txHash + " " + address + " " + value + " in";
	}

	/**
	 * Generate a record in the transaction dataset
	 *
	 * @param txHash
	 *            Transaction hash
	 * @param address
	 *            Output bitcoin address
	 * @param value
	 *            Number of Satoshi transferred
	 * @return A record of the output
	 */
	private String generateOutputRecord(String txHash,
			String address, long value) {
		return txHash + " " + address + " " + value + " out";
	}
}
