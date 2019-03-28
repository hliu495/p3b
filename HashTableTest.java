// TODO: add imports as needed
import static org.junit.jupiter.api.Assertions.*; // org.junit.Assert.*;
import org.junit.jupiter.api.Assertions;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import java.util.Random;



/**
 * This class is the JUnit test class for HashTable
 * 
 * @author Han Liu
 *
 */
public class HashTableTest {

  // create new instances of HashTable
  HashTable<Integer, String> hashTableInstance;
  HashTable<Integer, String> hashTableInstance2;

  /**
   * add code that runs before each test method
   * 
   * @throws Exception
   */
  @Before
  public void setUp() throws Exception {
    hashTableInstance = new HashTable<Integer, String>();
    hashTableInstance2 = new HashTable<Integer, String>(4, 0.75);
  }


  /**
   * add code that runs after each test method
   * 
   * @throws Exception
   */
  @After
  public void tearDown() throws Exception {
    hashTableInstance = null;
    hashTableInstance2 = null;


  }

  /**
   * Tests that a HashTable returns an integer code indicating which collision resolution strategy
   * is used. REFER TO HashTableADT for valid collision scheme codes.
   */
  @Test
  public void test000_collision_scheme() {
    HashTableADT htIntegerKey = new HashTable<Integer, String>();
    int scheme = htIntegerKey.getCollisionResolution();
    if (scheme < 1 || scheme > 9)
      fail("collision resolution must be indicated with 1-9");
  }

  /**
   * IMPLEMENTED AS EXAMPLE FOR YOU Tests that insert(null,null) throws IllegalNullKeyException
   */
  @Test
  public void test001_IllegalNullKey() {
    try {
      HashTableADT htIntegerKey = new HashTable<Integer, String>();
      htIntegerKey.insert(null, null);
      fail("should not be able to insert null key");
    } catch (IllegalNullKeyException e) {
      /* expected */ } catch (Exception e) {
      fail("insert null key should not throw exception " + e.getClass().getName());
    }
  }

  /**
   * Test insert duplicate keys throws DuplicateKeyException
   */
  @Test
  public void test002_InsertDuplicate() {
    try {
      hashTableInstance.insert(1, "one");
      hashTableInstance.insert(1, "another one");
      fail("should not be able to insert duplicate keys");
    } catch (DuplicateKeyException e) {
      /* expected */ } catch (Exception e) {
      fail("insert duplicate key should not throw exception " + e.getClass().getName());
    }
  }

  /**
   * Test remove null key throws IllegalNullKeyException
   */
  @Test
  public void test003_removeNullKey() {
    try {
      hashTableInstance.insert(1, "one");
      hashTableInstance.remove(null);
      fail("should not be able to remove null keys");
    } catch (IllegalNullKeyException e) {
      /* expected */ } catch (Exception e) {
      fail("remove null key should not throw exception " + e.getClass().getName());
    }
  }

  /**
   * Test get null key throws IllegalNullKeyException
   */
  @Test
  public void test004_getNullKey() {
    try {
      hashTableInstance.insert(1, "one");
      hashTableInstance.get(null);
      fail("should not be able to get null keys");
    } catch (IllegalNullKeyException e) {
      /* expected */ } catch (Exception e) {
      fail("get null key should not throw exception " + e.getClass().getName());
    }
  }

  /**
   * Test get a non-exist key throws KeyNotFoundException
   */
  @Test
  public void test005_getNonExistentKey() {
    try {
      hashTableInstance.insert(1, "one");
      hashTableInstance.get(2);
      fail("should not be able to get non-existent keys");
    } catch (KeyNotFoundException e) {
      /* expected */ } catch (Exception e) {
      fail("get non-existent key should not throw exception " + e.getClass().getName());
    }
  }

  /**
   * Test numKeys correct
   */
  @Test
  public void test006_getNumKeys() {
    try {
      if (hashTableInstance.numKeys() != 0) {
        fail("numKeys should be 0 when empty");
      }
      hashTableInstance.insert(1, "one");
      hashTableInstance.insert(2, "two");
      hashTableInstance.insert(3, "three");
      hashTableInstance.insert(4, "four");
      hashTableInstance.insert(5, "five");
      // numKeys should be 5
      if (hashTableInstance.numKeys() != 5) {
        fail("numKeys not correct after insert");
      }
      // after remove, numKeys should be 4
      hashTableInstance.remove(1);
      if (hashTableInstance.numKeys() != 4) {
        fail("numKeys not correct after remove");
      }
    } catch (Exception e) {
      fail("get numKeys should not throw exception " + e.getClass().getName());
    }
  }

  /**
   * Test load factor correct when there is no collision
   */
  @Test
  public void test007_getNoCollisionLoadFactor() {
    try {
      hashTableInstance.insert(1, "one");
      hashTableInstance.insert(2, "two");
      hashTableInstance.insert(3, "three");
      // [null,a1,2,3,null,null,null...]loadFactor should be 3/11
      if (hashTableInstance.getLoadFactor() != (double) 3 / 11) {
        fail("loadFactor is not correct when there is no collision");
      }
    } catch (Exception e) {
      fail("get loadFactore should nort throw exception " + e.getClass().getName());
    }
  }

  /**
   * Test load factor correct when there is collision
   */
  @Test
  public void test008_getWithCollisionLoadFactor() {
    try {
      hashTableInstance.insert(1, "one");
      hashTableInstance.insert(12, "one");
      hashTableInstance.insert(25, "three");
      // [null,1->12,null,25,null,null,null...] , loadFactor should be 3/11
      if (hashTableInstance.getLoadFactor() != (double) 3 / 11) {
        fail("loadFactor is not correct when there is collision");
      }
    } catch (Exception e) {
      fail("get loadFactore should nort throw exception " + e.getClass().getName());
    }
  }

  /**
   * Test get value without collision
   */
  @Test
  public void test009_getWithoutCollision() {
    try {
      hashTableInstance.insert(1, "one");
      hashTableInstance.insert(2, "two");
      hashTableInstance.insert(3, "three");
      // [null,1,2,3,null,null,null,null...]
      if (!hashTableInstance.get(2).equals("two")) {
        fail("get value is not correct when there is no collision");
      }
    } catch (Exception e) {
      fail("get value should nort throw exception " + e.getClass().getName());
    }
  }

  /**
   * Test get value with collision
   */
  @Test
  public void test010_getWithCollision() {
    try {
      hashTableInstance.insert(1, "one");
      hashTableInstance.insert(12, "one");
      hashTableInstance.insert(23, "one");
      // [null,1->12->23,null,null,null,null...]
      if (!hashTableInstance.get(12).equals("one")) {
        // get the value in the bucket
        fail("get value is not correct when there is collision");
      }
    } catch (Exception e) {
      fail("get value should nort throw exception " + e.getClass().getName());
    }
  }

  /**
   * Test remove without collision
   */
  @Test
  public void test011_removeWithoutCollision() {
    try {
      hashTableInstance.insert(1, "one");
      hashTableInstance.insert(2, "two");
      hashTableInstance.insert(3, "three");
      // [null,1,2,3,null,null,null...]
      if (!hashTableInstance.remove(2)) {
        // should return true; [null,1,null,3,null,null...]
        fail("remove failed");
      }
      if (hashTableInstance.numKeys != 2) {
        fail("remove failed to decreas number of keys");
      }
    } catch (Exception e) {
      fail("get value should nort throw exception " + e.getClass().getName());
    }
  }

  /**
   * Test remove with collision
   */
  @Test
  public void test012_removeWithCollision() {
    try {
      hashTableInstance.insert(1, "one");
      hashTableInstance.insert(12, "one");
      hashTableInstance.insert(23, "one");
      // [null,1->12->23,null,null,null...]
      if (!hashTableInstance.remove(12)) {
        // should return true; [null,1->23,null,null,null...]
        fail("remove failed");
      }
      if (hashTableInstance.numKeys != 2) {
        fail("remove failed to decreas number of keys");
      }
    } catch (Exception e) {
      fail("get value should nort throw exception " + e.getClass().getName());
    }
  }

  /**
   * Test expand hash table without collision
   */
  @Test
  public void test013_expandTableWithoutCollision() {
    try {
      // the initial size is 4, and the load factor threshold is 0.75
      hashTableInstance2.insert(1, "one");
      hashTableInstance2.insert(2, "two");
      hashTableInstance2.insert(3, "three");
      // [null,1,2,3], load factor = threshold, expand array. the size 2*4+1 = 9
      // [null,1,2,3,null,null,null,null,null]
      if (hashTableInstance2.getCapacity() != 9) {
        fail("expand hashTable failed");
      }
      if (hashTableInstance2.numKeys != 3) {
        // after expand the hash table, numKeys should still be 3
        fail("copy keys failed after expand the array");
      }
    } catch (Exception e) {
      fail("expand hashTable should nort throw exception " + e.getClass().getName());
    }
  }

  /**
   * Test expand hash table with collision
   */
  @Test
  public void test014_expandTableWithCollision() {
    try {
      // the initial size is 4, and the load factor threshold is 0.75
      hashTableInstance2.insert(1, "one");
      hashTableInstance2.insert(5, "one");
      hashTableInstance2.insert(9, "one");
      // [null,1->5->9,null,null], load factor = threshold, expand array. the size 2*4+1 = 9
      // [9,1,null,null,null,5,null,null,null]
      if (hashTableInstance2.getCapacity() != 9) {
        fail("expand hashTable failed");
      }
      if (hashTableInstance2.numKeys != 3) {
        // after expand the hash table, numKeys should still be 3
        fail("copy keys failed after expand the array");
      }
    } catch (Exception e) {
      fail("expand hashTable should nort throw exception " + e.getClass().getName());
    }
  }


}
