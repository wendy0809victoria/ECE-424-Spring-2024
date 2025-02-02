package main;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.Math;

public class UserCluster {
	private Map<Long, List<String>> userMap; // Map a user id to a list of
												// bitcoin addresses
	private Map<String, Long> keyMap; // Map a bitcoin address to a user id
	private Map<String, List<String>> hashMap;
	private Map<String, List<String>> addrMap;

	String file;

	int hash_index = 0;
	int addr_index = 1;
	int val_index  = 2;
	int type_index = 3;
	int column_len = 4;

	int userMapCt = 0;
	int keyMapCt = 0;
	long id_ct = 0;

	public UserCluster() {
		userMap = new HashMap<Long, List<String>>();
		keyMap = new HashMap<String, Long>();
		hashMap = new HashMap<String, List<String>>();
		addrMap = new HashMap<String, List<String>>();
		userMapCt = 0;
		keyMapCt = 0;
	}

	/**
	 * Read transactions from file
	 *
	 * @param file
	 * @return true if read succeeds; false otherwise
	 */
	public boolean readTransactions(String file) {
		// TODO implement me
		this.file = file;
		return true;
	}

	/**
	 * Merge addresses based on joint control
	 */
	public void mergeAddresses() {
		String line;
		String hash;
		String address;
		String io;
		try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
			while ((line = reader.readLine()) != null) {
				String[] parts = line.split("\\s+");
				if (parts.length == 4) {
					hash = parts[0];
					address = parts[1];
					io = parts[3];
					if (io.equals("in")) {
						if (!hashMap.containsKey(hash)) {
							List<String> addresses = new ArrayList<>();
							addresses.add(address);
							hashMap.put(hash, addresses);
						} else {
							List<String> addresses = hashMap.get(hash);
							addresses.add(address);
						}
						if (!addrMap.containsKey(address)) {
							List<String> hashes = new ArrayList<>();
							hashes.add(hash);
							addrMap.put(address, hashes);
						} else {
							List<String> hashes = addrMap.get(address);
							hashes.add(hash);
						}
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		// System.out.println(addrMap.size());
		// System.out.println(hashMap.size());

		Map<String, Long> hashId = new HashMap<String, Long>();
		for (String input : addrMap.keySet()) {
			List<String> hashes = addrMap.get(input);
			boolean cluster = false;
			long cluster_id = 0;
			String cluster_hash = "";
			for (String hash_id : hashes) {
				if (hashId.containsKey(hash_id)) {
					cluster = true;
					cluster_id = hashId.get(hash_id);
					cluster_hash = hash_id;
					break;
				}
			}
			if (!cluster) {
				for (String hash_id : hashes) {
					hashId.put(hash_id, id_ct);
				}
				id_ct++;
			} else {
				for (String hash_id : hashes) {
					if (hash_id != cluster_hash) {
						hashId.put(hash_id, cluster_id);
					}
				}
			}
		}

		// System.out.println(id_ct);

		try (BufferedReader reader_in = new BufferedReader(new FileReader(file))) {
			while ((line = reader_in.readLine()) != null) {
				String[] parts = line.split("\\s+");
				if (parts.length == 4) {
					hash = parts[0];
					address = parts[1];
					io = parts[3];
					if (io.equals("in")) {
						long userId = hashId.get(hash);
						keyMap.put(address, userId);
						if (!userMap.containsKey(userId)) {
							List<String> addresses = new ArrayList<>();
							addresses.add(address);
							userMap.put(userId, addresses);
						} else {
							List<String> addresses = userMap.get(userId);
							if (!addresses.contains(address)) {
								addresses.add(address);
							}
						}
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		try (BufferedReader reader_out = new BufferedReader(new FileReader(file))) {
			while ((line = reader_out.readLine()) != null) {
				String[] parts = line.split("\\s+");
				if (parts.length == 4) {
					hash = parts[0];
					address = parts[1];
					io = parts[3];
					if (io.equals("out")) {
						Long userId = keyMap.get(address);
						if (userId == null) {
							long newUserId = id_ct++;
							keyMap.put(address, newUserId);
							List<String> addresses = new ArrayList<>();
							addresses.add(address);
							userMap.put(newUserId, addresses);
						}
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		// System.out.println(keyMap.size());
		// System.out.println(userMap.size());
	}

	/**
	 * Return number of users (i.e., clusters) in the transaction dataset
	 *
	 * @return number of users (i.e., clusters)
	 */
	public int getUserNumber() {
		// TODO implement me
		return userMap.size();
	}

	/**
	 * Return the largest cluster size
	 *
	 * @return size of the largest cluster
	 */
	public int getLargestClusterSize() {
		// TODO implement me
		int largestSize = 0;
		for (List<String> addresses : userMap.values()) {
			int size = addresses.size();
			if (size > largestSize) {
				largestSize = size;
			}
		}
		return largestSize;
	}

	public boolean writeUserMap(String file) {
		try {
			BufferedWriter w = new BufferedWriter(new FileWriter(file));
			for (long user : userMap.keySet()) {
				List<String> keys = userMap.get(user);
				w.write(user + " ");
				for (String k : keys) {
					w.write(k + " ");
				}
				w.newLine();
			}
			w.flush();
			w.close();
		} catch (IOException e) {
			System.err.println("Error in writing user list!");
			e.printStackTrace();
			return false;
		}

		return true;
	}

	public boolean writeKeyMap(String file) {
		try {
			BufferedWriter w = new BufferedWriter(new FileWriter(file));
			for (String key : keyMap.keySet()) {
				w.write(key + " " + keyMap.get(key) + "\n");
				w.newLine();
			}
			w.flush();
			w.close();
		} catch (IOException e) {
			System.err.println("Error in writing key map!");
			e.printStackTrace();
			return false;
		}

		return true;
	}

	public boolean writeUserGraph(String txFile, String userGraphFile) {
	     try {
                        BufferedReader r1 = new BufferedReader(new FileReader(txFile));
                        Map<String, Long> txUserMap = new HashMap<String, Long>();
                        String nextLine;
                        while ((nextLine = r1.readLine()) != null) {
                                String[] s = nextLine.split(" ");
                                if (s.length < column_len) {
                                        System.err.println("Invalid format: " + nextLine);
                                        r1.close();
                                        return false;
                                }
                                if (s[type_index].equals("in") && !txUserMap.containsKey(s[hash_index])) { // new transaction
                                        Long user;
                                        if ((user=keyMap.get(s[addr_index])) == null) {
                                                System.err.println(s[addr_index] + " is not in the key map!");
                                                System.out.println(nextLine);
                                                r1.close();
                                                return false;
                                        }
                                        txUserMap.put(s[hash_index], user);
                                }
                        }
                        r1.close();

                        BufferedReader r2 = new BufferedReader(new FileReader(txFile));
                        BufferedWriter w = new BufferedWriter(new FileWriter(userGraphFile));
                        while ((nextLine = r2.readLine()) != null) {
                                String[] s = nextLine.split(" ");
                                if (s.length < column_len) {
                                        System.err.println("Invalid format: " + nextLine);
                                        r2.close();
                                        w.flush();
                                        w.close();
                                        return false;
                                }
                                if (s[type_index].equals("out")) {
                                        if(txUserMap.get(s[hash_index]) == null) {
                                                System.err.println("Did not find input transaction for Tx: " + s[hash_index]);
                                                r2.close();
                                                w.flush();
                                                w.close();
                                                return false;
                                        }
                                        long inputUser = txUserMap.get(s[hash_index]);
                                        Long outputUser;
                                        if ((outputUser=keyMap.get(s[addr_index])) == null) {
                                                System.err.println(s[addr_index] + " is not in the key map!");
                                                r2.close();
                                                w.flush();
                                                w.close();
                                                return false;
                                        }
                                        w.write(inputUser + "," + outputUser + "," + s[val_index] + "\n");
                                }
                        }
                        r2.close();
                        w.flush();
                        w.close();
                } catch (IOException e) {
                        e.printStackTrace();
                }

				// Graph Analysis Starts here
				String line;
				long oid = 0;
				long val = 0;
				long trade = 0;
				long targetId = 0;
				double toBTC = 176.6;					// BTC to $ tranversion on 10/25/2013
				long transVal = 28500000L;				// Total transaction value ($) seized by the FBI
				long toSatoshi = 100000000L;
				long minDiff = (long) (transVal/toBTC*toSatoshi);
				Map<Long, Long> tradeMap = new HashMap<Long, Long>();
				List<String> candidates = new ArrayList<>();
				List<String> ownerAddr1 = new ArrayList<>();
				List<String> ownerAddr2 = new ArrayList<>();
				List<String> ownerAddr3 = new ArrayList<>();

				try (BufferedReader reader_graph = new BufferedReader(new FileReader("userGraph.txt"))) {
					while ((line = reader_graph.readLine()) != null) {
						String[] parts = line.split(",");
						oid = Long.parseLong(parts[1]);
						val = Long.parseLong(parts[2]);
						if (!tradeMap.containsKey(oid)) {
							tradeMap.put(oid, val); 
						} else {
							trade = tradeMap.get(oid);
							trade = trade + val;
							tradeMap.remove(oid);
							tradeMap.put(oid, trade);
						}
					}
					for (Long id : tradeMap.keySet()) {
						if (Math.abs(16138165300000L - tradeMap.get(id)) < minDiff) {
							minDiff = Math.abs(16138165300000L - tradeMap.get(id));
							targetId = id;
						}
					}
					candidates = userMap.get(targetId);
					for (String can : candidates) {
						System.out.println("Seized address: " + can + "\n");
						try (BufferedWriter address_writer = new BufferedWriter(new FileWriter("seizedAddress.txt"))) {
							address_writer.write("Seized address: " + can + "\n");
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
				Map<Long, Long> ownerMap = new HashMap<Long, Long>();
				long iid = 0;
				long max = 0;
				long ownerId1= 0;
				long ownerId2= 0;
				long ownerId3= 0;
				try (BufferedReader reader_graph = new BufferedReader(new FileReader("userGraph.txt"))) {
					while ((line = reader_graph.readLine()) != null) {
						String[] parts = line.split(",");
						iid = Long.parseLong(parts[0]);
						oid = Long.parseLong(parts[1]);
						val = Long.parseLong(parts[2]);
						if (oid == targetId) {
							ownerMap.put(iid, val); 
						} 
					}
					for (Long owner : ownerMap.keySet()) {
						if (ownerMap.get(owner) > max) {
							max = ownerMap.get(owner);
							ownerId1 = owner;
						}
					}
					ownerMap.remove(ownerId1);
					max = 0;
					for (Long owner : ownerMap.keySet()) {
						if (ownerMap.get(owner) > max) {
							max = ownerMap.get(owner);
							ownerId2 = owner;
						}
					}
					ownerMap.remove(ownerId2);
					max = 0;
					for (Long owner : ownerMap.keySet()) {
						if (ownerMap.get(owner) > max) {
							max = ownerMap.get(owner);
							ownerId3 = owner;
						}
					}
					ownerMap.remove(ownerId3);
					ownerAddr1 = userMap.get(ownerId1);
					ownerAddr2 = userMap.get(ownerId2);
					ownerAddr3 = userMap.get(ownerId3);
					for (String o1 : ownerAddr1) {
						try (BufferedWriter address_writer = new BufferedWriter(new FileWriter("ownerAddress1.txt"))) {
							address_writer.write("Owner address 1: " + o1 + "\n");
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
					for (String o2 : ownerAddr2) {
						try (BufferedWriter address_writer = new BufferedWriter(new FileWriter("ownerAddress2.txt"))) {
							address_writer.write("Owner address 2: " + o2 + "\n");
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
					for (String o3 : ownerAddr3) {
						try (BufferedWriter address_writer = new BufferedWriter(new FileWriter("ownerAddress3.txt"))) {
							address_writer.write("Owner address 3: " + o3 + "\n");
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				} catch (IOException e) {
					e.printStackTrace();
				}

                return true;

	}
}
