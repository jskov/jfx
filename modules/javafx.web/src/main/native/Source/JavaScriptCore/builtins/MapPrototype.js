/*
 * Copyright (C) 2016-2017 Yusuke Suzuki <utatane.tea@gmail.com>.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in the
 *    documentation and/or other materials provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY APPLE INC. ``AS IS'' AND ANY
 * EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE ARE DISCLAIMED.  IN NO EVENT SHALL APPLE INC. OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY
 * OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

@constructor
@globalPrivate
function MapIterator(iteratedObject, kind)
{
    "use strict";

    @assert(@isMap(iteratedObject));
    @putByIdDirectPrivate(this, "iteratedObject", iteratedObject);
    @putByIdDirectPrivate(this, "mapIteratorKind", kind);
    @putByIdDirectPrivate(this, "mapBucket", @mapBucketHead(iteratedObject));
}

function values()
{
    "use strict";

    if (!@isMap(this))
        @throwTypeError("Map.prototype.values requires that |this| be Map");

    return new @MapIterator(this, @iterationKindValue);
}

function keys()
{
    "use strict";

    if (!@isMap(this))
        @throwTypeError("Map.prototype.keys requires that |this| be Map");

    return new @MapIterator(this, @iterationKindKey);
}

function entries()
{
    "use strict";

    if (!@isMap(this))
        @throwTypeError("Map.prototype.entries requires that |this| be Map");

    return new @MapIterator(this, @iterationKindKeyValue);
}

function forEach(callback /*, thisArg */)
{
    "use strict";

    if (!@isMap(this))
        @throwTypeError("Map operation called on non-Map object");

    if (typeof callback !== 'function')
        @throwTypeError("Map.prototype.forEach callback must be a function");

    var thisArg = @argument(1);
    var bucket = @mapBucketHead(this);

    do {
        bucket = @mapBucketNext(bucket);
        if (bucket === @sentinelMapBucket)
            break;
        callback.@call(thisArg, @mapBucketValue(bucket), @mapBucketKey(bucket), this);
    } while (true);
}
