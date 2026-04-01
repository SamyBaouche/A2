# COMP 249 – Assignment 3 (SmartTravel)

## Part 1 – Collections Foundation

### Why `LinkedList` for `RecentList<T>` instead of `ArrayList`?
`RecentList<T>` represents a **fixed-size history** of “most recently viewed” items.

Operations we do most often:
- add a new item to the **front** (most recent) → `addFirst(...)`
- remove the **oldest** item from the end when capacity exceeded → `removeLast()`

A `LinkedList` is a good fit because adding/removing at the **head/tail** is natural and does not require shifting elements as an `ArrayList` does.

> Note: both structures are fine for max size 10, but `LinkedList` matches the assignment’s requirement and expresses the intent clearly.

