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
        _id : "fillContentElements" ,
        value : function (arr) {
            return db.contentElements.find({$or:[{$and : [{ref : "toto"}, {type : "EDITABLE"}]},{$and : [{ref : "tata"}, {type : "EDITABLE"}]}]});
            var orCondition = [];
            for(var i = 0; i < arr.length; i++){
                print(i);
                orCondition.push({$and : [{ref : ""+arr[i].ref}, {type : ""+arr[i].type}]});
            }
            var ret = db.contentElements.find( { $or: orCondition } );
            db.toto.save({_id:"readme",value: "yo"});
            return ret;
        }
    }
);
fillContentElements([ {
    "ref" : "toto",
    "type" : "EDITABLE",
    "value" : null
} ]);
/*a = db.contentElements.find({$or:[{$and : [{ref : "toto"}, {type : "EDITABLE"}]},{$and : [{ref : "tata"}, {type : "EDITABLE"}]}]});
{
    "ref" : "toto",
    "type" : "EDITABLE",
    "value" : null
}