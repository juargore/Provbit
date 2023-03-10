package ai.bright.provbit.demo.adapters.realm.models

import io.realm.RealmObject
import ai.bright.provbit.types.uuidString

class RealmItem: RealmObject {
    var guid: String = ""
    var name: String = ""
    var desc: String = ""
}
