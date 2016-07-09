# CollectorEx
This library adds new coolectors with similar functionality like SQL window functions. Example:
```java
List<Integer> list = Arrays.asList(1, 2, 3, 4, 4, 3, 2, 1);

SortedMap<Integer, List<Integer>> rankedMap = list.stream().collect(CollectorEx.rank());
SortedMap<Integer, List<Integer>> denseRankedMap = list.stream().collect(CollectorEx.denseRank());

System.out.println(rankedMap);
System.out.println(denseRankedMap);
// will print
//{1=[1, 1], 3=[2, 2], 5=[3, 3], 7=[4, 4]}
//{1=[1, 1], 2=[2, 2], 3=[3, 3], 4=[4, 4]}
```
