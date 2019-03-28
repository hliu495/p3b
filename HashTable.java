import java.util.ArrayList;
/**
 * This class is the implementation of a hashTable, using array of liked nodes: when there is a
 * collision, insert the new element to the bucket of the certain index.
 * 
 * @author Han Liu
 *
 * @param <K> the key of the element
 * @param <V> the value of the element
 */
public class HashTable<K extends Comparable<K>, V> implements HashTableADT<K, V> {

  /**
   * This class a a private class of linked nodes
   * 
   * @author Han Liu
   *
   * @param <K> the key of the node
   * @param <V> the value of the node
   */
  private class ListNode<K, V> {
    private K key; // key of the node
    private V value; // value of the node
    private ListNode<K, V> nextNode; // linked to next node

    /**
     * Constructor of ListNode
     * 
     * @param key   is key of the node
     * @param value is the value stored in the node
     * @param next  linked to the next node
     */
    private ListNode(K key, V value, ListNode next) {
      this.key = key;
      this.value = value;
      this.nextNode = next;
    }
  }

  ListNode<K, V>[] hashTable; // the array represents the hash table
  int numKeys; // number of keys in the hash table
  int capacity; // the size of the array, size of the hash table
  double loadFactorThreshold; // threshold is the load factor that causes a resize and rehash

  /**
   * Default no-argument constructor
   * 
   */
  public HashTable() {
    // default: initialize capacity to 11 and threshold to 0.75
    hashTable = new ListNode[11];
    capacity = 11;
    loadFactorThreshold = 0.75;
    numKeys = 0;
  }

  /**
   * The constructor can initialize capacity and load factor threshold
   * 
   * @param initialCapacity     is the initial capacity of the hash table
   * @param loadFactorThreshold is the load factor that causes a resize and rehash
   */
  public HashTable(int initialCapacity, double loadFactorThreshold) {
    capacity = initialCapacity;
    this.loadFactorThreshold = loadFactorThreshold;
    hashTable = new ListNode[initialCapacity];
    numKeys = 0;
  }

  /**
   * Using the hashCode() method to generate a hash index for given element !!The index should be an
   * absolute value
   * 
   * @param key is the key of the element
   * @return the hash index of the element
   */
  private Integer hashFunction(K key) {
    return Math.abs(key.hashCode() % getCapacity());
  }

  /**
   * Resize(expand) the hash table to (2 * capacity + 1)
   * 
   * @param hashTable the current hashTable to be expanded
   * @throws IllegalNullKeyException
   * @throws DuplicateKeyException
   */
  private void expandArray(ListNode<K, V>[] hashTable)
      throws IllegalNullKeyException, DuplicateKeyException {
    this.capacity = 2 * this.capacity + 1; // expand the array to the (2 * capacity + 1)
    ListNode<K, V>[] currentHashTable = hashTable; // copy the old hashTable here
    this.hashTable = new ListNode[getCapacity()]; // update the hashTable to the larger hashTable
    this.numKeys = 0; // update the numKeys
    // copy all elements in the small hash table to the larger one
    for (int i = 0; i < currentHashTable.length; i++) {
      // when there is an element at the current index, copy it to the larger hash table
      if (currentHashTable[i] != null) {
        ListNode<K, V> currentNode = currentHashTable[i];
        // when there is element under the bucket of the current index, copy all element in the
        // bucket to the larger hash table by traversing the linked list
        while (currentNode != null) {
          insert(currentNode.key, currentNode.value);
          currentNode = currentNode.nextNode;
        }
      }
    }
  }

  /**
   * Check if the given key is already in the hash table
   * 
   * @param key is the key of the element
   * @return true when the key is in the hash table, return false otherwise
   */
  private boolean contains(K key) {
    int position = hashFunction(key); // get the hash index of the given key
    if (hashTable[position] == null) { // if there is no element at the hash index, the given key is
                                       // not in the hash table, return false
      return false;
    }
    // if there is a bucket at the hash index, traversing the linked list to check if the key has
    // already in the hash table
    ListNode<K, V> currentNode = hashTable[position];
    while (currentNode != null) {
      if (currentNode.key.equals(key)) { // when the key is found, return true
        return true;
      } else {
        currentNode = currentNode.nextNode;
      }
    }
    return false;
  }

  /**
   * insert the element into the hash table
   * 
   * @param key   is the key of the new element
   * @param value is the value of the new element
   */
  @Override
  public void insert(K key, V value) throws IllegalNullKeyException, DuplicateKeyException {
    // if the key is null, throw IllegalNullKeyException
    if (key == null) {
      throw new IllegalNullKeyException();
    }
    // if the has already in the hash table, throw DuplicateKeyException
    if (contains(key)) {
      throw new DuplicateKeyException();
    }
    ListNode<K, V> newNode = new ListNode<>(key, value, null);
    int position;
    position = hashFunction(key); // get the hash index of the given key
    // if there is no element at the index, insert the new element to that index
    if (hashTable[position] == null) {
      hashTable[position] = newNode;
      numKeys++;
    } else {
      // otherwise handle the collision: inserting the element into the bucket under the hash index
      ListNode<K, V> currentNode = hashTable[position];
      while (currentNode.nextNode != null) {
        currentNode = currentNode.nextNode;
      }
      currentNode.nextNode = newNode;
      numKeys++;
    }
    // extend array when necessary: when load factor equals to or greater than the threshold
    if (getLoadFactor() >= loadFactorThreshold) {
      expandArray(hashTable); // call the expandArray method
    }
  }

  /**
   * remove the element from the hash table
   * 
   * @param key is the key of element to be removed
   * @throws IllegalNullKeyException when key is null
   * @return true if remove successfully, false otherwise
   */
  @Override
  public boolean remove(K key) throws IllegalNullKeyException {
    // if key is null, throws IllegalNullKeyException
    if (key == null) {
      throw new IllegalNullKeyException();
    }
    int position = hashFunction(key); // get the hash index of the element to be removed
    ListNode<K, V> currentNode = hashTable[position];
    if (currentNode == null) { // if the key is not in the expected index, remove failed
      return false;
    } else if (currentNode.key.equals(key)) {
      // when key is found at the hash index, removed it from the hash table
      hashTable[position] = currentNode.nextNode;
      numKeys--;
      return true;
    }
    // traversing the bucket when necessary to find the element to be removed
    while (currentNode.nextNode != null) {
      if (currentNode.nextNode.key.equals(key)) {
        // when key is found in the bucket under the hash index, remove from the bucket
        currentNode.nextNode = currentNode.nextNode.nextNode;
        numKeys--;
        return true;
      }
      currentNode = currentNode.nextNode;
    }
    return false; // if key is not found, remove failed
  }

  @Override
  /**
   * get the value of the element in the hash table
   * 
   * @param key is the key of the element
   * @throws IllegalNullKeyException when key is null
   * @throws KeyNotFoundException    when key is not found
   */
  public V get(K key) throws IllegalNullKeyException, KeyNotFoundException {
    // if key is null, throws IllegalNullKeyException
    if (key == null) {
      throw new IllegalNullKeyException();
    }
    int position = hashFunction(key); // get the hash index of the element
    // traversing the linked list under the hash index
    ListNode<K, V> currentNode = hashTable[position];
    while (currentNode != null) {
      if (currentNode.key.equals(key)) {
        // when key is found, return the value
        return currentNode.value;
      } else {
        // otherwise keep traversing
        currentNode = currentNode.nextNode;
      }
    }
    throw new KeyNotFoundException();// after traversing, if key is not found, throws
                                     // KeyNotFoundException
  }

  /**
   * return the number of keys in the hash table
   * 
   * @return the number of keys
   */
  @Override
  public int numKeys() {
    return this.numKeys;
  }

  /**
   * return the load factor threshold
   * 
   * @return the load factor threshold
   */
  @Override
  public double getLoadFactorThreshold() {
    return this.loadFactorThreshold;
  }

  /**
   * return the load factor
   * 
   * @return the load factor
   */
  @Override
  public double getLoadFactor() {
    return (double) numKeys / capacity;
  }

  /**
   * return the capacity of the hash table
   * 
   * @return the size of the hash table
   */
  @Override
  public int getCapacity() {
    return this.capacity;
  }

  /**
   * indicate the method used for hash table
   * 
   * @return 5 indicates this hash table is an array of linked list
   */
  @Override
  public int getCollisionResolution() {
    // 1 OPEN ADDRESSING: linear probe
    // 2 OPEN ADDRESSING: quadratic probe
    // 3 OPEN ADDRESSING: double hashing
    // 4 CHAINED BUCKET: array of arrays
    // 5 CHAINED BUCKET: array of linked nodes
    // 6 CHAINED BUCKET: array of search trees
    // 7 CHAINED BUCKET: linked nodes of arrays
    // 8 CHAINED BUCKET: linked nodes of linked node
    // 9 CHAINED BUCKET: linked nodes of search trees
    // I use an array of linked nodes
    return 5;
  }
}
