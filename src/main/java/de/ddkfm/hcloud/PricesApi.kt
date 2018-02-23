package de.ddkfm.hcloud

import com.mashape.unirest.http.Unirest
import de.ddkfm.hcloud.de.ddkfm.hcloud.models.*

class PricesApi(token : String) : ApiBase(token = token) {

    // get one specific data center like fsn ngb
    fun getOPrice(id: Int): price {
        var req = Unirest
                .get("$endpoint/pricing" + id)
                .header("Authorization", auth);
        val jsonResp = req
                .asJson()
                .body
                .`object`;
        val jsonPrice = jsonResp.getJSONObject("pricing");

        var ServerPrice = price(
                currency = jsonPrice.getString("currency"),
                varRate = jsonPrice.getString("vat_rate"),
                image = priceInformation(
                        net = jsonResp.getJSONObject("image").getJSONObject("price_per_gb_month").getString("net"),
                        gross = jsonResp.getJSONObject("image").getJSONObject("price_per_gb_month").getString("gross")
                ),
                floatingIP = priceInformation(
                        net = jsonResp.getJSONObject("floating_ip").getJSONObject("price_monthly").getString("net"),
                        gross = jsonResp.getJSONObject("floating_ip").getJSONObject("price_monthly").getString("gross")
                ),
                traffic = priceInformation(
                        net = jsonResp.getJSONObject("traffic").getJSONObject("price_per_tb").getString("net"),
                        gross = jsonResp.getJSONObject("traffic").getJSONObject("price_per_tb").getString("gross")
                ),
                ServerBackup = jsonResp.getJSONObject("server_backup").getString("percentage"),
                ServerTypes = ServerPrice(
                        id = jsonResp.getJSONObject("server_types").getInt("id"),
                        name = jsonResp.getJSONObject("server_types").getString("name"),
                        price = pricesServerModel(
                                location = jsonResp.getJSONObject("server_types").getJSONObject("prices").getString("location"),
                                priceHour = priceInformation(
                                        net = jsonResp.getJSONObject("server_types").getJSONObject("prices").getJSONObject("price_hourly").getString("net"),
                                        gross = jsonResp.getJSONObject("server_types").getJSONObject("prices").getJSONObject("price_hourly").getString("gross")
                                ),
                                priceMonth = priceInformation(
                                        net = jsonResp.getJSONObject("server_types").getJSONObject("prices").getJSONObject("price_monthly").getString("net"),
                                        gross = jsonResp.getJSONObject("server_types").getJSONObject("prices").getJSONObject("price_monthly").getString("net")
                                )
                        )
                )
        )
        return ServerPrice;
    }
}