package edu.caltech.cs2.project04;

import edu.caltech.cs2.datastructures.LinkedDeque;
import edu.caltech.cs2.datastructures.TrieMap;
import edu.caltech.cs2.helpers.Inspection;
import edu.caltech.cs2.helpers.Reflection;
import edu.caltech.cs2.helpers.RuntimeInstrumentation;
import edu.caltech.cs2.interfaces.IDeque;
import org.hamcrest.MatcherAssert;
import org.hamcrest.collection.IsIterableContainingInAnyOrder;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.lang.reflect.Constructor;
import java.time.Duration;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class HashMapTrieTest {
    private final int SINGLE_OP_TIMEOUT_MS = 25;
    private final int CONTAINS_VALUE_TIMEOUT_MS = 60;
    private static String STRING_SOURCE = "src/edu/caltech/cs2/datastructures/TrieMap.java";

    private static TrieMap newTrie() {
        Constructor c = Reflection.getConstructor(TrieMap.class, Function.class);
        Function<IDeque<Object>, Iterable<Object>> collector = (IDeque<Object> o) -> {
            IDeque<Object> k = new LinkedDeque<>();
            for (Object m : o) {
                k.addBack(m);
            }

            return k;
        };

        return Reflection.newInstance(c, collector);
    }

    private static TrieMap<Object, Iterable<Object>, Object> trieFromMap(Map<Iterable<Object>, Object> testSet) {
        TrieMap<Object, Iterable<Object>, Object> t = newTrie();

        for (Map.Entry<Iterable<Object>, Object> e: testSet.entrySet()) {
            t.put(e.getKey(), e.getValue());
        }

        return t;
    }

    private static Map<Iterable<Object>, Object> getBooleanTestPoints(int size) {
        Random rand = new Random(2019010310);
        Map<Iterable<Object>, Object> testOne = new HashMap<>();

        for (int i = 0; i < size; i++) {
            List<Object> k = new ArrayList<>();

            for (int j = 0; j < 1 + rand.nextInt(20); j++) {
                k.add(rand.nextBoolean());
            }

            /*
            if (rand.nextBoolean() && rand.nextBoolean()) {
                testOne.put(k, null);
                continue;
            }
            */

            List<Integer> v = new ArrayList<>();

            for (int j = 0; j < 1 + rand.nextInt(10); j++) {
                v.add(rand.nextInt());
            }

            testOne.put(k, v);
        }

        return testOne;
    }

    private static Stream<Arguments> testSetData() {
        Random rand = new Random(2019010310);
        Map<Iterable<Object>, Object> testOne = getBooleanTestPoints(5000);
        Map<List<Integer>, Integer> testTwo = new HashMap<>();

        for (int i = 0; i < 5000; i++) {
            List<Integer> k = new ArrayList<>();

            for (int j = 0; j < 1 + rand.nextInt(20); j++) {
                k.add(rand.nextInt(5));
            }

            /*
            if (rand.nextBoolean()) {
                testTwo.put(k, null);
                continue;
            }
            */

            testTwo.put(k, rand.nextInt());
        }


        Arguments[] args = {Arguments.of(testOne), Arguments.of(testTwo)};
        return Stream.of(args);
    }

    @Order(0)
    @Tag("C")
    @DisplayName("Test put(K key, V value) and get(K key)")
    @ParameterizedTest(name = "Testing Insertion and Access on {0}.")
    @MethodSource("testSetData")
    void testInsertionAndAccess(Map<Iterable<Object>, Object> testSet) {
        TrieMap<Object, Iterable<Object>, Object> t = trieFromMap(testSet);

        for (Map.Entry<Iterable<Object>, Object> e: testSet.entrySet()) {
            assertTimeout(Duration.ofMillis(SINGLE_OP_TIMEOUT_MS), () -> {
                assertEquals(t.get(e.getKey()), e.getValue());
            });
        }
    }

    @Order(1)
    @Tag("A")
    @DisplayName("Test remove(K key)")
    @ParameterizedTest(name = "Test remove on {0}.")
    @MethodSource("testSetData")
    void testRemove(Map<Iterable<Object>, Object> testSet) {
        Random rand = new Random(2019010310);

        TrieMap<Object, Iterable<Object>, Object> t = trieFromMap(testSet);

        // Test remove elements that are in the map
        List<Iterable<Object>> keys = new ArrayList<>();
        Set<Iterable<Object>> keySet = testSet.keySet();
        List<Iterable<Object>> removedArr = new ArrayList<>();

        keys.addAll(keySet);

        int runs = (int) (keys.size() * 0.25);

        for (int i = 0; i < runs; i++) {
            int inx = rand.nextInt(keys.size());
            Iterable<Object> removed = keys.remove(inx);

            assertTimeout(Duration.ofMillis(3 * SINGLE_OP_TIMEOUT_MS), () -> {
                Object v = t.remove(removed);

                assertEquals(testSet.get(removed), v,
                        "Removing a key that exists does not return the right value.");

                assertEquals(t.get(removed), null, "Removing a key does not delete the pair.");
            });


            removedArr.add(removed);
        }

        // Test remove elements not in the map
        for (Iterable<Object> k : removedArr) {
            assertTimeout(Duration.ofMillis(SINGLE_OP_TIMEOUT_MS), () -> {
                assertEquals(t.remove(k), null,
                        "Removing a key that does not exist does not return null.");
            });
        }

        for (Iterable<Object> k : keys) {
            assertEquals(t.get(k), testSet.get(k), "Element not removed is changed.");
        }

    }

    private void containsKeyHelper(Map<Iterable<Object>, Object> testSet, boolean testRemove) {
        TrieMap<Object, Iterable<Object>, Object> t = newTrie();
        List<List<Object>> keys = new ArrayList<>();

        Random rand = new Random(2019101010);

        Set<List<Object>> disallowKey = new HashSet<>();

        // If this test does not test removal, then we need to disallow some keys from being
        // added to test containsKey returning false.
        for (Map.Entry<Iterable<Object>, Object> e : testSet.entrySet()) {
            if (rand.nextDouble() > 0.3) {
                continue;
            }

            List<Object> kList = iterableToList(e.getKey());
            disallowKey.add(kList);
        }

        for (Map.Entry<Iterable<Object>, Object> e : testSet.entrySet()) {
            // If we are testing remove, these keys will be removed, instead of not added.
            if (disallowKey.contains(e.getKey()) && !testRemove) {
                continue;
            }

            t.put(e.getKey(), e.getValue());
            keys.add(iterableToList(e.getKey()));
        }

        for (List<Object> k : keys) {
            assertTimeout(Duration.ofMillis(SINGLE_OP_TIMEOUT_MS), () -> {
                assertTrue(t.containsKey(k), "Running containsKey on added key returns false.");
            });
        }

        if (!testRemove) {
            for (List<Object> k : disallowKey) {
                assertTimeout(Duration.ofMillis(SINGLE_OP_TIMEOUT_MS), () -> {
                    assertFalse(t.containsKey(k),
                            "Running containsKey on key that hasn't been added returns true.");
                });
            }
            return;
        }

        for (List<Object> k : disallowKey) {
            t.remove(k);
            assertTimeout(Duration.ofMillis(SINGLE_OP_TIMEOUT_MS), () -> {
                assertFalse(t.containsKey(k),
                        "Key that has been removed still returns true from containsKey.");
            });
        }
    }

    @Order(1)
    @Tag("C")
    @DisplayName("Test containsKey(K key) -- add")
    @ParameterizedTest(name = "Test containsKey (add) on {0}.")
    @MethodSource("testSetData")
    void containsKeyAdd(Map<Iterable<Object>, Object> testSet) {
        containsKeyHelper(testSet, false);
    }

    @Order(1)
    @Tag("A")
    @DisplayName("Test containsKey(K key) -- add and remove")
    @ParameterizedTest(name = "Test containsKey (add & remove) on {0}.")
    @MethodSource("testSetData")
    void containsKeyAddRemove(Map<Iterable<Object>, Object> testSet) {
        containsKeyHelper(testSet, true);
    }

    private void testContainsValueHelper(Map<Iterable<Object>, Object> testSet, boolean testRemove) {
        TrieMap<Object, Iterable<Object>, Object> t = newTrie();
        final int num_tests = 200;

        Random rand = new Random(2019101010);

        Set<Object> disallowVal = new HashSet<>();
        List<Object> values = new ArrayList<>();
        List<Iterable<Object>> disallowKeys = new ArrayList<>();

        // If this test does not test removal, then we need to disallow some values from being
        // added to test containsValue returning false.
        for (Map.Entry<Iterable<Object>, Object> e : testSet.entrySet()) {
            if (rand.nextDouble() > 0.3) {
                continue;
            }

            Object val = e.getValue();
            disallowVal.add(val);
        }

        for (Map.Entry<Iterable<Object>, Object> e : testSet.entrySet()) {
            // If we are testing remove, these keys will be removed, instead of not added.
            if (disallowVal.contains(e.getValue())) {
                disallowKeys.add(e.getKey());

                if (!testRemove) {
                    continue;
                }
            }

            t.put(e.getKey(), e.getValue());
            values.add(e.getValue());
        }

        int tests = 0;

        for (Object v : values) {
            if (tests > num_tests) break;

            assertTimeout(Duration.ofMillis(CONTAINS_VALUE_TIMEOUT_MS), () -> {
                assertTrue(t.containsValue(v), "Running containsValue on added value returns false.");
            });

            tests++;
        }


        if (!testRemove) {
            tests = 0;

            for (Object v : disallowVal) {
                if (tests > num_tests) break;

                assertTimeout(Duration.ofMillis(CONTAINS_VALUE_TIMEOUT_MS), () -> {
                    assertFalse(t.containsValue(v),
                            "Running containsValue on value that hasn't been added returns true.");
                });

                tests++;
            }
            return;
        }

        // Remove keys corresponding to values that need to be removed.
        for (Iterable<Object> k : disallowKeys) {
            t.remove(k);
        }

        tests = 0;

        for (Object v : disallowVal) {
            if (tests > num_tests) break;

            assertTimeout(Duration.ofMillis(CONTAINS_VALUE_TIMEOUT_MS), () -> {
                assertFalse(t.containsValue(v),
                        "Value that has been removed still returns true from containsValue.");
            });

            tests++;
        }
    }

    @Order(1)
    @Tag("A")
    @DisplayName("Test containsValue(V value) -- add and remove")
    @ParameterizedTest(name = "Test containsValue (add & remove) on {0}.")
    @MethodSource("testSetData")
    void testContainsValueAddRemove(Map<Iterable<Object>, Object> testSet) {
        testContainsValueHelper(testSet, true);
    }

    @Order(1)
    @Tag("C")
    @DisplayName("Test containsValue(V value) -- add")
    @ParameterizedTest(name = "Test containsValue (add) on {0}.")
    @MethodSource("testSetData")
    void testContainsKeyAdd(Map<Iterable<Object>, Object> testSet) {
        testContainsValueHelper(testSet, false);
    }

    private TrieMap<Object, Iterable<Object>, Object> createPrefixRestrictedTrie(Map<Iterable<Object>, Object> testSet,
                                                                                 Set<List<Object>> disallowPrefix, List<List<Object>> keys, boolean testRemove) {
        TrieMap<Object, Iterable<Object>, Object> t = newTrie();
        Random rand = new Random(2019101010);

        for (Map.Entry<Iterable<Object>, Object> e : testSet.entrySet()) {
            if (rand.nextDouble() < 0.1) {
                List<Object> kList = iterableToList(e.getKey());

                if (kList.size() > 5) {
                    int l = 5 + rand.nextInt(kList.size() - 5);
                    disallowPrefix.add(kList.subList(0, l));
                }
            }
        }

        // Add all non-disallowPrefix elements to t.
        for (Map.Entry<Iterable<Object>, Object> e: testSet.entrySet()) {
            List<Object> kList = iterableToList(e.getKey());
            boolean shouldBreak = false;

            for (int i = 0; i <= kList.size(); i++) {
                // If we are testing remove, these values will be removed.
                if (disallowPrefix.contains(kList.subList(0, i)) && !testRemove) {
                    shouldBreak = true;
                }
            }

            if (shouldBreak) {
                continue;
            }

            t.put(e.getKey(), e.getValue());
            keys.add(iterableToList(e.getKey()));
        }

        return t;
    }

    private void testIsPrefixHelper(Map<Iterable<Object>, Object> testSet, boolean testRemove) {
        List<List<Object>> keys = new ArrayList<>();
        Set<List<Object>> disallowPrefix = new HashSet<>();

        TrieMap<Object, Iterable<Object>, Object> t = createPrefixRestrictedTrie(testSet, disallowPrefix, keys, testRemove);

        // Test that all prefixes in the keys of the trie have isPrefix returning true.
        for (List<Object> k : keys) {
            for (int i = 1; i <= k.size(); i++) {
                assertTrue(t.isPrefix(k.subList(0, i)), "Prefix that was added returning false from isPrefix.");
            }
        }


        // Test that the prefixes that weren't added return false from isPrefix, and end the test.
        if (!testRemove) {
            for (List<Object> p : disallowPrefix) {
                assertFalse(t.isPrefix(p), "Prefix that wasn't added returns true from isPrefix.");
            }

            return;
        }

        for (List<Object> p : disallowPrefix) {
            for (List<Object> k : keys) {
                if (p.size() > k.size()) {
                    continue;
                }

                if (k.subList(0, p.size()).equals(p)) {
                    t.remove(k);
                }
            }

            assertFalse(t.isPrefix(p), "Prefix that was removed still exists in the tree.");
        }
    }

    @Order(1)
    @Tag("C")
    @DisplayName("Test isPrefix() -- add.")
    @ParameterizedTest(name = "Test isPrefix add on {0}.")
    @MethodSource("testSetData")
    void testIsPrefixAdd(Map<Iterable<Object>, Object> testSet) {
        testIsPrefixHelper(testSet, false);
    }

    @Order(1)
    @Tag("A")
    @DisplayName("Test isPrefix() -- add and remove.")
    @ParameterizedTest(name = "Test isPrefix add on {0}.")
    @MethodSource("testSetData")
    void testIsPrefixAddRemove(Map<Iterable<Object>, Object> testSet) {
        testIsPrefixHelper(testSet, true);
    }

    private List<List<Object>> getCompletionsNaive(List<List<Object>> keys, List<Object> prefix) {
        List<List<Object>> matching = new ArrayList<>();

        for (List<Object> k : keys) {
            if (k.size() < prefix.size()) continue;

            if (k.subList(0, prefix.size()).equals(prefix)) {
                matching.add(k);
            }
        }

        return matching;
    }

    private void testGetForPrefixAccuracy(Map<Iterable<Object>, Object> testSet,
                                          List<List<Object>> keys, TrieMap<Object, Iterable<Object>, Object> t) {
        Random rand = new Random(2019101010);

        for (int i = 0; i < 50; i++) {
            int kInx = rand.nextInt(keys.size());
            List<Object> key = keys.get(kInx);

            int prefixLen = 1 + rand.nextInt(key.size());
            List<Object> prefix = key.subList(0, prefixLen);

            List<Object> expected = new ArrayList<>();
            for (List<Object> k : getCompletionsNaive(keys, prefix)) {
                expected.add(testSet.get(k));
            }

            MatcherAssert.assertThat(t.getCompletions(prefix),
                    IsIterableContainingInAnyOrder.containsInAnyOrder(expected.toArray()));
        }
    }

    private void testGetForPrefixHelper(Map<Iterable<Object>, Object> testSet, boolean testRemove) {
        List<List<Object>> keys = new ArrayList<>();
        Set<List<Object>> disallowPrefix = new HashSet<>();

        TrieMap<Object, Iterable<Object>, Object> t = createPrefixRestrictedTrie(testSet, disallowPrefix, keys, testRemove);

        // Test that getCompletions is accurate after additions.
        testGetForPrefixAccuracy(testSet, keys, t);

        if (!testRemove) {
            for (List<Object> p : disallowPrefix) {
                MatcherAssert.assertThat("Prefix that doesn't exists returns non-empty array from getCompletions",
                        t.getCompletions(p), IsIterableContainingInAnyOrder.containsInAnyOrder(new ArrayList<>()));
            }

            return;
        }

        Set<List<Object>> newKeys = new HashSet<>(keys);

        for (List<Object> p : disallowPrefix) {
            for (List<Object> k : keys) {
                if (p.size() > k.size()) {
                    continue;
                }

                if (k.subList(0, p.size()).equals(p)) {
                    t.remove(k);
                    newKeys.remove(k);
                    continue;
                }
            }

            MatcherAssert.assertThat("Prefix that doesn't exists returns non-empty array from getCompletions",
                    t.getCompletions(p), IsIterableContainingInAnyOrder.containsInAnyOrder(new ArrayList<>()));

        }


        testGetForPrefixAccuracy(testSet, new ArrayList<>(newKeys), t);
    }

    @Order(1)
    @Tag("C")
    @DisplayName("Test getCompletions() -- add.")
    @ParameterizedTest(name = "Test getCompletions add on {0}.")
    @MethodSource("testSetData")
    void testGetForPrefixAdd(Map<Iterable<Object>, Object> testSet) {
        testGetForPrefixHelper(testSet, false);
    }

    @Order(1)
    @Tag("A")
    @DisplayName("Test getCompletions() -- add and remove.")
    @ParameterizedTest(name = "Test getCompletions add and remove on {0}.")
    @MethodSource("testSetData")
    void testGetForPrefixAddRemove(Map<Iterable<Object>, Object> testSet) {
        testGetForPrefixHelper(testSet, true);
    }

    private List<Object> iterableToList(Iterable<Object> iter) {
        List<Object> keyList = new ArrayList<>();
        for (Object kp : iter) {
            keyList.add(kp);
        }

        return keyList;
    }

    @Order(1)
    @Tag("A")
    @DisplayName("Test iterator()")
    @ParameterizedTest(name = "Test iterator on {0}.")
    @MethodSource("testSetData")
    void iterator(Map<Iterable<Object>, Object> testSet) {
        TrieMap<Object, Iterable<Object>, Object> t = newTrie();
        List<Object> expected = new ArrayList<>();

        for (Map.Entry<Iterable<Object>, Object> e: testSet.entrySet()) {
            t.put(e.getKey(), e.getValue());
            expected.add(iterableToList(e.getKey()));
        }

        List<Object> keys = new ArrayList<>();
        for (Iterable<Object> key: t) {
            keys.add(iterableToList(key));
        }

        MatcherAssert.assertThat(keys,
                IsIterableContainingInAnyOrder.containsInAnyOrder(expected.toArray()));

    }

    @Order(1)
    @Tag("A")
    @DisplayName("Test values()")
    @ParameterizedTest(name = "Test values on {0}.")
    @MethodSource("testSetData")
    void valuesCollection(Map<Iterable<Object>, Object> testSet) {
        TrieMap<Object, Iterable<Object>, Object> t = newTrie();
        List<Object> expected = new ArrayList<>();

        for (Map.Entry<Iterable<Object>, Object> e: testSet.entrySet()) {
            t.put(e.getKey(), e.getValue());
            expected.add(e.getValue());
        }

        List<Object> values = new ArrayList<>();
        for (Object value: t.values()) {
            values.add(value);
        }

        MatcherAssert.assertThat(values,
                IsIterableContainingInAnyOrder.containsInAnyOrder(expected.toArray()));

    }

    private void testSizeHelper(Map<Iterable<Object>, Object> testSet, boolean testRemove) {
        Random rand = new Random(2019010310);

        TrieMap<Object, Iterable<Object>, Object> t = newTrie();

        // Test size with insertions.
        int size = 0;

        for (Map.Entry<Iterable<Object>, Object> e : testSet.entrySet()) {
            t.put(e.getKey(), e.getValue());
            size += 1;

            final int s = size;
            assertEquals(t.size(), s, "Adding elements does not appropriately change the size.");
        }


        List<Iterable<Object>> keys = new ArrayList<>();
        Set<Iterable<Object>> keySet = testSet.keySet();
        List<Iterable<Object>> removedArr = new ArrayList<>();

        keys.addAll(keySet);

        int tests = 0;

        for (Map.Entry<Iterable<Object>, Object> e : testSet.entrySet()) {
            if (tests > 100) break;

            t.put(e.getKey(), e.getValue());

            final int s = size;

            assertTimeout(Duration.ofMillis(SINGLE_OP_TIMEOUT_MS), () -> {
                assertEquals(t.size(), s, "Adding elements that already exist changes the size.");
            });

            tests++;
        }

        if (!testRemove) {
            return;
        }

        // Test size with deletions
        int runs = (int) (keys.size() * 0.5);

        for (int i = 0; i < runs; i++) {
            int inx = rand.nextInt(keys.size());
            Iterable<Object> removed = keys.remove(inx);
            Object v = t.remove(removed);
            size -= 1;

            final int s = size;

            assertEquals(t.size(), s,
                    "Removing a key that exists does not decrease size appropriately.");
            removedArr.add(removed);
        }

        // Test remove elements not in the map
        for (Iterable<Object> k : removedArr) {
            t.remove(k);
            assertEquals(t.size(), size, "Removing a key that does not exist changes the size.");
        }
    }

    @Order(2)
    @Tag("A")
    @DisplayName("Test size() -- complexity")
    @Test
    void testSizeComplexity() {
        Function<Integer, TrieMap<Object, Iterable<Object>, Object>> provide = (Integer numElements) -> {
            Map<Iterable<Object>, Object> o = getBooleanTestPoints(numElements);
            TrieMap<Object, Iterable<Object>, Object> t = trieFromMap(o);
            return t;
        };

        Consumer<TrieMap<Object, Iterable<Object>, Object>> size = (TrieMap<Object, Iterable<Object>, Object> t) -> t.size();
        RuntimeInstrumentation.assertAtMost("size",
                RuntimeInstrumentation.ComplexityType.CONSTANT, provide, size, 8);
    }

    @Order(2)
    @Tag("A")
    @DisplayName("Test size() -- add and remove")
    @ParameterizedTest(name = "Test size on {0}.")
    @MethodSource("testSetData")
    void testSizeAddRemove(Map<Iterable<Object>, Object> testSet) {
        testSizeHelper(testSet, true);
    }

    @Order(2)
    @Tag("C")
    @DisplayName("Test size() -- add")
    @ParameterizedTest(name = "Test size on {0}.")
    @MethodSource("testSetData")
    void testSizeAdd(Map<Iterable<Object>, Object> testSet) {
        testSizeHelper(testSet, false);
    }

    @Order(0)
    @Tag("C")
    @DisplayName("Does not use or import disallowed classes")
    @Test
    public void testForInvalidClasses() {
        List<String> regexps = List.of("java.util.(?!Iterator|function.Function)", "java.lang.reflect", "java.io");
        Inspection.assertNoImportsOf(STRING_SOURCE, regexps);
        Inspection.assertNoUsageOf(STRING_SOURCE, regexps);
    }
}