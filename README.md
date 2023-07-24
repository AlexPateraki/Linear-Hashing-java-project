# Linear-Hashing-java-project
Linear Hashing is a memory-based data structure that supports the following operations: 
1. Insertion of a random key: Allows adding a new key to the linear hash table. Initially, the table has a size of M = 100 positions, with each position having a capacity of 10 keys. If a position overflows, an overflow page is created to handle additional keys.  
2. Search for a random key: Enables searching for a key in the linear hash table. The hash function is used to locate the position of the key in the table, and if necessary, the overflow pages are searched as well.  
3. Deletion of a random key: Allows removing a key from the linear hash table. When a key is deleted, the table may become fragmented, and under certain criteria, merging of underutilized pages can occur to optimize space usage.

The split criterion for insertion (page split) is determined at the beginning of the method's operation and depends on the "load factor" u, which can be either u > 50% or u > 80%. This criterion determines when the table should be split into new pages to accommodate more keys efficiently. The merge criterion for deletion (page merging) is defined as u < 50%, indicating that when a page becomes underutilized, it can be merged with another page to reduce fragmentation and improve space utilization.

In summary, Linear Hashing is a memory-based data structure that dynamically handles the insertion, search, and deletion of keys using a hash table with overflow pages. It uses specific load factor criteria for both splitting and merging pages to optimize performance and memory usage.
