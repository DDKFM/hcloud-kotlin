package de.ddkfm.hcloud

import com.mashape.unirest.http.Unirest
import de.ddkfm.hcloud.de.ddkfm.hcloud.models.*
import org.json.JSONObject

class ActionApi (token : String) : ApiBase(token = token) {

    // get all available actions
    fun getActions() : List<Action> {
        var req = Unirest
                .get("$endpoint/actions")
                .header("Authorization", auth);
        val jsonResp = req
                .asJson()
                .body
                .`object`;
        val actions = jsonResp.getJSONArray("datacenters");
        var returnList = mutableListOf<Action>();
        actions.forEach {
            //Kotlin-Magic: "it" is automatically the current element in the JSONArray
            val jsonAction: JSONObject = it as JSONObject;
            var OneAction = Action(
                    id = jsonAction.getInt("id"),
                    command = jsonAction.getString("command"),
                    status = jsonAction.getString("status"),
                    progress = jsonAction.getInt("progress"),
                    started = jsonAction.getString("started"),
                    finished = jsonAction.getString("finished"),
                    resources = Resource(
                            id = jsonAction.getJSONObject("resources").getInt("id"),
                            type = jsonAction.getJSONObject("resources").getString("type")
                    ),
                    error = Error(
                            code = jsonAction.getJSONObject("error").getString("code"),
                            message = jsonAction.getJSONObject("error").getString("message")
                    )
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

        var action = Action(
                id = jsonAction.getInt("id"),
                command = jsonAction.getString("command"),
                status = jsonAction.getString("status"),
                progress = jsonAction.getInt("progress"),
                started = jsonAction.getString("started"),
                finished = jsonAction.getString("finished"),
                resources = Resource(
                        id = jsonAction.getJSONObject("resources").getInt("id"),
                        type = jsonAction.getJSONObject("resources").getString("type")
                ),
                error = Error(
                        code = jsonAction.getJSONObject("error").getString("code"),
                        message = jsonAction.getJSONObject("error").getString("message")
                )
        )
        return action
    }
}
