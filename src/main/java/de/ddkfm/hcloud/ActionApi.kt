package de.ddkfm.hcloud

import com.mashape.unirest.http.Unirest
import de.ddkfm.hcloud.de.ddkfm.hcloud.models.*
import de.ddkfm.hcloud.models.Action
import de.ddkfm.hcloud.models.Resource
import de.ddkfm.hcloud.models.ServerStatus
import org.json.JSONObject
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class ActionApi (token : String) : ApiBase(token = token) {

    // get all available actions
    fun getActions() : List<Action> {
        val jsonResp = this.get("/actions", null)
        val actions = jsonResp?.getJSONArray("actions") ?: return emptyList();
        var returnList = mutableListOf<Action>();
        val formatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME
        actions.forEach {
            //Kotlin-Magic: "it" is automatically the current element in the JSONArray
            val jsonAction: JSONObject = it as JSONObject;
            var OneAction = Action(
                    id = jsonAction.getInt("id"),
                    command = jsonAction.getString("command"),
                    status = ServerStatus.valueOf(jsonAction.getString("status")),
                    progress = jsonAction.getInt("progress"),
                    started = LocalDateTime.parse(jsonAction.getString("started"), formatter),
                    finished = LocalDateTime.parse(jsonAction.getString("finished"), formatter),
                    resources = jsonAction.getJSONArray("resources").map {
                                val jsonResource = it as JSONObject
                                Resource(
                                    id = jsonResource.getInt("id"),
                                    type = jsonResource.getString("type")
                                )
                    },
                    error = if(!jsonAction.isNull("error")) {
                                de.ddkfm.hcloud.models.Error(
                                    code = jsonAction.getJSONObject("error").getString("code"),
                                    message = jsonAction.getJSONObject("error").getString("message")
                                )
                             }else null
            )
            returnList.add(OneAction);
        }
        return returnList
    }


    // get action from specified server
    fun getOneAction(id: Int): Action {
        var req = Unirest
                .get("$endpoint/actions/" + id)
                .header("Authorization", auth);
        val jsonResp = req
                .asJson()
                .body
                .`object`;
        val jsonAction = jsonResp.getJSONObject("action");
        val formatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME
        var action = Action(
                id = jsonAction.getInt("id"),
                command = jsonAction.getString("command"),
                status = ServerStatus.valueOf(jsonAction.getString("status")),
                progress = jsonAction.getInt("progress"),
                started = LocalDateTime.parse(jsonAction.getString("started"), formatter),
                finished = LocalDateTime.parse(jsonAction.getString("finished"), formatter),
                resources = jsonAction.getJSONArray("resources").map {
                                Resource(
                                    id = jsonAction.getJSONObject("resources").getInt("id"),
                                    type = jsonAction.getJSONObject("resources").getString("type")
                                )
                            },
                error = de.ddkfm.hcloud.models.Error(
                        code = jsonAction.getJSONObject("error").getString("code"),
                        message = jsonAction.getJSONObject("error").getString("message")
                )
        )
        return action
    }
}
