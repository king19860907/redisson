/**
 * Copyright 2014 Nikita Koksharov, Nickolay Borbit
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.redisson.core;

import java.util.concurrent.TimeUnit;

/**
 * <p>Map-based cache with ability to set TTL for each entry via
 * {@link #put(Object, Object, long, TimeUnit)} or {@link #putIfAbsent(Object, Object, long, TimeUnit)}
 * And therefore has an complex lua-scripts inside.</p>
 *
 * <p>Current redis implementation doesnt have map entry eviction functionality.
 * Thus entries are checked for TTL expiration during any key/value/entry read operation.
 * If key/value/entry expired then it doesn't returns and clean task runs asynchronous.
 * Clean task deletes removes 100 expired entries at once.
 * In addition there is {@link org.redisson.EvictionScheduler}. This scheduler
 * deletes expired entries in time interval between 5 seconds to 2 hours.</p>
 *
 * <p>If eviction is not required then it's better to use {@link org.redisson.reactive.RedissonMapReactive}.</p>
 *
 * @author Nikita Koksharov
 *
 * @param <K> key
 * @param <V> value
 */
public interface RMapCache<K, V> extends RMap<K, V>, RMapCacheAsync<K, V> {

    /**
     * If the specified key is not already associated
     * with a value, associate it with the given value.
     * <p/>
     * Stores value mapped by key with specified time to live.
     * Entry expires after specified time to live.
     * If the map previously contained a mapping for
     * the key, the old value is replaced by the specified value.
     *
     * @param key
     * @param value
     * @param ttl - time to live for key\value entry.
     *              If <code>0</code> then stores infinitely.
     * @param unit
     * @return previous associated value
     */
    V putIfAbsent(K key, V value, long ttl, TimeUnit unit);

    /**
     * Stores value mapped by key with specified time to live.
     * Entry expires after specified time to live.
     * If the map previously contained a mapping for
     * the key, the old value is replaced by the specified value.
     *
     * @param key
     * @param value
     * @param ttl - time to live for key\value entry.
     *              If <code>0</code> then stores infinitely.
     * @param unit
     * @return previous associated value
     */
    V put(K key, V value, long ttl, TimeUnit unit);

    /**
     * Returns the number of entries in cache.
     * This number can reflects expired entries too
     * due to non realtime cleanup process.
     *
     */
    @Override
    int size();

}
