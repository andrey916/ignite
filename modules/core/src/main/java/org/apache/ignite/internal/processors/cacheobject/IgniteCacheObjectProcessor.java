/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.ignite.internal.processors.cacheobject;

import org.apache.ignite.*;
import org.apache.ignite.cluster.*;
import org.apache.ignite.configuration.*;
import org.apache.ignite.internal.*;
import org.apache.ignite.internal.processors.*;
import org.apache.ignite.internal.processors.cache.*;
import org.apache.ignite.lang.*;
import org.jetbrains.annotations.*;

/**
 * Cache objects processor.
 */
public interface IgniteCacheObjectProcessor extends GridProcessor {
    /**
     * @see GridComponent#onKernalStart()
     */
    public void onCacheProcessorStarted();

    /**
     * @param typeName Type name.
     * @return Type ID.
     */
    public int typeId(String typeName);

    /**
     * @param obj Object to get type ID for.
     * @return Type ID.
     */
    public int typeId(Object obj);

    /**
     * Converts temporary off-heap object to heap-based.
     *
     * @param ctx Context.
     * @param obj Object.
     * @return Heap-based object.
     * @throws IgniteException In case of error.
     */
    @Nullable public Object unwrapTemporary(GridCacheContext ctx, @Nullable Object obj) throws IgniteException;

    /**
     * Prepares cache object for cache (e.g. copies user-provided object if needed).
     *
     * @param obj Cache object.
     * @param cctx Cache context.
     * @return Object to be store in cache.
     */
    @Nullable public CacheObject prepareForCache(@Nullable CacheObject obj, GridCacheContext cctx);

    /**
     * Checks whether object is portable object.
     *
     * @param obj Object to check.
     * @return {@code True} if object is already a portable object, {@code false} otherwise.
     */
    public boolean isPortableObject(Object obj);

    /**
     * Checks whether given class is portable.
     *
     * @param cls Class.
     * @return {@code true} If the class was registered as portable.
     */
    public boolean isPortableClass(Class<?> cls);

    /**
     * @param obj Portable object to get field from.
     * @param fieldName Field name.
     * @return Field value.
     */
    public Object field(Object obj, String fieldName);

    /**
     * Checks whether field is set in the object.
     *
     * @param obj Object.
     * @param fieldName Field name.
     * @return {@code true} if field is set.
     */
    public boolean hasField(Object obj, String fieldName);

    /**
     * @param ctx Cache object context.
     * @param val Value.
     * @return Value bytes.
     * @throws IgniteCheckedException If failed.
     */
    public byte[] marshal(CacheObjectContext ctx, Object val) throws IgniteCheckedException;

    /**
     * @param ctx Context.
     * @param bytes Bytes.
     * @param clsLdr Class loader.
     * @return Unmarshalled object.
     * @throws IgniteCheckedException If failed.
     */
    public Object unmarshal(CacheObjectContext ctx, byte[] bytes, ClassLoader clsLdr) throws IgniteCheckedException;

    /**
     * @param node Node.
     * @param cacheName Cache name.
     * @return Cache object context.
     */
    public CacheObjectContext contextForCache(ClusterNode node, @Nullable String cacheName,
        @Nullable CacheConfiguration ccfg);

    /**
     * @param ctx Cache context.
     * @param obj Key value.
     * @param userObj If {@code true} then given object is object provided by user and should be copied
     *        before stored in cache.
     * @return Cache key object.
     */
    public KeyCacheObject toCacheKeyObject(CacheObjectContext ctx, Object obj, boolean userObj);

    /**
     * @param ctx Cache context.
     * @param obj Object.
     * @param userObj If {@code true} then given object is object provided by user and should be copied
     *        before stored in cache.
     * @return Cache object.
     */
    @Nullable public CacheObject toCacheObject(CacheObjectContext ctx, @Nullable Object obj, boolean userObj);

    /**
     * @param ctx Cache context.
     * @param type Object type.
     * @param bytes Object bytes.
     * @return Cache object.
     */
    public CacheObject toCacheObject(CacheObjectContext ctx, byte type, byte[] bytes);

    /**
     * @param ctx Context.
     * @param valPtr Value pointer.
     * @param tmp If {@code true} can return temporary instance which is valid while entry lock is held.
     * @return Cache object.
     * @throws IgniteCheckedException If failed.
     */
    public CacheObject toCacheObject(GridCacheContext ctx, long valPtr, boolean tmp) throws IgniteCheckedException;

    /**
     * @param obj Value.
     * @return {@code True} if object is of known immutable type of it is marked
     *          with {@link IgniteImmutable} annotation.
     */
    public boolean immutable(Object obj);

    /**
     * @param cacheName Cache name.
     * @return {@code True} if portable format should be preserved when passing values to cache store.
     */
    public boolean keepPortableInStore(@Nullable String cacheName);
}
