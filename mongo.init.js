db.counters.insert(
    {
        _id: "pageKey",
        seq: 0
    }
);
db.system.js.save(
    {
        _id : "getNextSequence" ,
        value : function (name) {
            var ret = db.counters.findAndModify(
                {
                    query: { _id: name },
                    update: { $inc: { seq: 1 } },
                    new: true
                }
            );

            return ret.seq;
        }
    }
);


db.system.js.save(
    {
        _id : "fillContentElement" ,
        value : function (ref,type,value) {
            db.contentElements.findAndModify({
                query: { ref: ref, type: type },
                update: { ref: ref, type: type, value: value},
                upsert: true
            })
        }
    }
);
