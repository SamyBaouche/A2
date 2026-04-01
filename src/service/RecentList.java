package service;

import java.util.LinkedList;

/**
 * RecentList<T>
 *
 * Generic fixed-size "recently viewed" history list.
 * Internally uses a {@link LinkedList}.
 *
 * Requirements:
 * - addRecent(T item): add to front; if capacity > 10 remove oldest from end
 * - printRecent(int maxToShow): print up to N items (most recent first)
 * - size(), isEmpty()
 */
public class RecentList<T> {
	private final LinkedList<T> list = new LinkedList<>();
	private static final int MAX_SIZE = 10;

	public void addRecent(T item) {
		if (item == null) {
			return;
		}

		list.addFirst(item);
		if (list.size() > MAX_SIZE) {
			list.removeLast();
		}
	}

	public void printRecent(int maxToShow) {
		if (maxToShow <= 0 || list.isEmpty()) {
			System.out.println("No recent items.");
			return;
		}

		int limit = Math.min(maxToShow, list.size());
		for (int i = 0; i < limit; i++) {
			System.out.println((i + 1) + ". " + list.get(i));
		}
	}

	public int size() {
		return list.size();
	}

	public boolean isEmpty() {
		return list.isEmpty();
	}
}


