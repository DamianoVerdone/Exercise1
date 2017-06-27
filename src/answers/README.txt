PALINDROME
----------

Two cursors one at the beginning of the string and one at the end.
The algorithm check one by one if the char under each cursor are equal.
As soon It finds a couple of chars different it returns false.


K-COMPLEMENTARY-PAIRS
---------------------

Loop once on the input array and store the elements in a Map structure where the key is the input Integer
and the value is a List of the value positions in the original array.
Loop again on the original array, and check if the Map contains the element (K - current_value)
If (K - current_value) is present in the map, loop on the list of positions and add the Pairs
(OriginalArray[current_pos], OriginalArray[ListOfPositionValue])
in the result List only if current_pos < ListOfPositionValue


TOP-PHRASES
-----------

Because the file is too big the idea is to stream the file line by line.
This is done using an helper class RandomFileIterator.
The algorithm calculates the hash (md5) of each phrase.
The Md5 result is used as a key in a Hashmap, the value is the number of occurrences for the same phrase plus the
line (offset from the beginning of the file)  and the column (we can have at max 50 phrases for a line)
where this phrase was found in the file the first time.
When all the file is scanned. A PriorityQueue structure is used to filter the top 100,000 phrases.
The file is accessed again to translate the line and the column value in the actual phrase.

