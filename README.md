# CollectorEx
This library provides new coolectors for using with java 8 streams. Some collectors are provide similar functionality like SQL window functions.
###Rank collector
Rank collector calculates the rank for stream of objects using given comparator. If objects are implements Comparable interface then comparator may be omitted. Equal objects receive the same rank. Number of tied rows added to the next rank. Therefore, the ranks may not be consecutive numbers. To produce consecutive numbers use dense rank collector. Here is example of rank and dense rank comparators which returns sorted map with ranks as a key and list of objects as a value for corresponding key:
```java
List<Integer> list = Arrays.asList(1, 2, 3, 4, 4, 3, 2, 1);

SortedMap<Integer, List<Integer>> rankedMap = list.stream().collect(CollectorEx.rank());
SortedMap<Integer, List<Integer>> denseRankedMap = list.stream().collect(CollectorEx.denseRank());

System.out.println(rankedMap);
System.out.println(denseRankedMap);
// will print
// {1=[1, 1], 3=[2, 2], 5=[3, 3], 7=[4, 4]}
// {1=[1, 1], 2=[2, 2], 3=[3, 3], 4=[4, 4]}
```
Alternatively objects can be mapped to ranks:
```java
List<Integer> list = Arrays.asList(-1, -2, -3, -4, -4, -3, -2, -1);

Map<Integer, Integer> rankedMap = list.stream().collect(CollectorEx.mapObjToRank());
Map<Integer, Integer> denseRankedMap = list.stream().collect(CollectorEx.mapObjToDenseRank());

System.out.println(rankedMap);
System.out.println(denseRankedMap);
// will print
// {-1=7, -2=5, -3=3, -4=1}
// {-1=4, -2=3, -3=2, -4=1}
```
If required custom collector can be provided:
```java
List<Integer> list = Arrays.asList(1, 2, 3, 4, 4, 3, 2, 1);

SortedMap<Integer, Set<Integer>> rankedMap = list.stream().collect(CollectorEx.rank(Collectors.toSet()));

System.out.println(rankedMap);
// will print
// {1=[1], 3=[2], 5=[3], 7=[4]}
```
And finally the full version with custom comparator, rank comparator, dense rank flag and custom collector:
```java
List<Integer> list = Arrays.asList(1, 2, 3, 4, 4, 3, 2, 1);
Comparator<Integer> intComparator = Integer::compare;

SortedMap<Integer, Set<Integer>> rankedMap = list.stream()
              .collect(CollectorEx.rank(intComparator, intComparator.reversed(), false, Collectors.toSet()));

System.out.println(rankedMap);
// will print
// {7=[4], 5=[3], 3=[2], 1=[1]}
```
###NTile collector
NTile collector divides stream of objects into a number of buckets using given comparator. If objects are implements Comparable interface then comparator may be omitted. Default collector returns map with tile number as a key and list of objects as a value for corresponding number.
```java
List<Integer> list = Arrays.asList(null, 1, 1, 2, 3, null);

Map<Integer, List<Integer>> result = list.stream().collect(CollectorEx.ntile(2));

System.out.println(result);
// will print
// {1=[1, 1, 2], 2=[3, null, null]}
```
Custom comparator may be provided for sorting. Here is example of custom comparator which puts nulls before non null values:
```java
List<Integer> list = Arrays.asList(null, 1, 1, 2, 3, null);
Comparator<Integer> integerComparator = Comparator.nullsFirst(Integer::compareTo);

Map<Integer, List<Integer>> result = list.stream().collect(CollectorEx.ntile(2, integerComparator));

System.out.println(result);
// will print
// {1=[null, null, 1], 2=[1, 2, 3]}
```
Custom collector can be passed as downstream collector:
```java
List<Integer> list = Arrays.asList(null, 1, 1, 2, 3, null);

Map<Integer, Set<Integer>> result = list.stream().collect(CollectorEx.ntile(2, toSet()));
System.out.println(result);
// will print
// {1=[1, 2], 2=[null, 3]}
```
###Distinct collector
Distinct collector return distinct elements of stream using given comparator. Internally it uses rank collector and takes first element for each rank. Because rank collector sorts element ditinct collector doesn't preserve original stream elements order.
```java
List<Integer> list = Arrays.asList(1, 2, 2, 1, -1, null, null);

List<Integer> result = list.stream().collect(CollectorEx.distinct());

System.out.println(result);
//will print
//[-1, 1, 2, null]
```
Custom mapping function can be passed to collector to apply for stream elements:
```java
List<Integer> list = Arrays.asList(1, -1, 2, -2, 3);

List<Integer> result = list.stream().collect(CollectorEx.distinct(i ->  i * i));

System.out.println(result);
// will print
// [1, 4, 9]
```
