package com.broken_e.utils;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pool;
import com.badlogic.gdx.utils.Pool.Poolable;

public abstract class BePool<T> {

	public final int max;

	private final Array<T> freeObjects;

	/** Creates a pool with an initial capacity of 16 and no maximum. */
	public BePool() {
		this(16, Integer.MAX_VALUE);
	}

	/** Creates a pool with the specified initial capacity and no maximum. */
	public BePool(int initialCapacity) {
		this(initialCapacity, Integer.MAX_VALUE);
	}

	/**
	 * @param max
	 *            The maximum number of free objects to store in this pool.
	 */
	public BePool(int initialCapacity, int max) {
		freeObjects = new Array<T>(false, initialCapacity);
		this.max = max;
	}

	abstract protected T newObject();

	/**
	 * Returns an object from this pool. The object may be new (from {@link #newObject()}) or reused (previously
	 * {@link #free(Object) freed}).
	 */
	public T obtain() {
		T object = freeObjects.size == 0 ? newObject() : freeObjects.removeIndex(0);
		if (object instanceof BePoolable)
			((BePoolable) object).start();
		return object;
	}

	/**
	 * Puts the specified object in the pool, making it eligible to be returned by {@link #obtain()}. If the pool
	 * already contains {@link #max} free objects, the specified object is reset but not added to the pool.
	 */
	public void free(T object) {
		if (object == null)
			throw new IllegalArgumentException("object cannot be null.");
		if (freeObjects.size < max)
			freeObjects.add(object);
		if (object instanceof BePoolable)
			((BePoolable) object).reset();
	}

	/**
	 * Puts the specified objects in the pool. Null objects within the array are silently ignored.
	 * 
	 * @see #free(Object)
	 */
	public void freeAll(Array<T> objects) {
		if (objects == null)
			throw new IllegalArgumentException("object cannot be null.");
		for (int i = 0; i < objects.size; i++) {
			T object = objects.get(i);
			if (object == null)
				continue;
			if (freeObjects.size < max)
				freeObjects.add(object);
			if (object instanceof Poolable)
				((Poolable) object).reset();
		}
	}

	/** Removes all free objects from this pool. */
	public void clear() {
		freeObjects.clear();
	}

	/** Objects implementing this interface will have {@link #reset()} called when passed to {@link #free(Object)}. */
	static public interface BePoolable {
		/** Resets the object for reuse. Object references should be nulled and fields may be set to default values. */
		public void reset();

		/** called when object is obtained */
		public void start();
	}
}