package de.ddkfm.hcloud

import com.mashape.unirest.http.Unirest
import de.ddkfm.hcloud.de.ddkfm.hcloud.models.*

class PricesApi(token : String) : ApiBase(token = token) {

    // get all prices by hetzner cloud
    fun getPrice(id: Int): price {
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
                        net = jsonPrice.getJSONObject("image").getJSONObject("price_per_gb_month").getString("net"),
                        gross = jsonPrice.getJSONObject("image").getJSONObject("price_per_gb_month").getString("gross")
                ),
                floatingIP = priceInformation(
                        net = jsonPrice.getJSONObject("floating_ip").getJSONObject("price_monthly").getString("net"),
                        gross = jsonPrice.getJSONObject("floating_ip").getJSONObject("price_monthly").getString("gross")
                ),
                traffic = priceInformation(
                        net = jsonPrice.getJSONObject("traffic").getJSONObject("price_per_tb").getString("net"),
                        gross = jsonPrice.getJSONObject("traffic").getJSONObject("price_per_tb").getString("gross")
                ),
                ServerBackup = jsonPrice.getJSONObject("server_backup").getString("percentage"),
                ServerTypes = ServerPrice(
                        id = jsonPrice.getJSONObject("server_types").getInt("id"),
                        name = jsonPrice.getJSONObject("server_types").getString("name"),
                        price = pricesServerModel(
                                location = jsonPrice.getJSONObject("server_types").getJSONObject("prices").getString("location"),
                                priceHour = priceInformation(
                                        net = jsonPrice.getJSONObject("server_types").getJSONObject("prices").getJSONObject("price_hourly").getString("net"),
                                        gross = jsonPrice.getJSONObject("server_types").getJSONObject("prices").getJSONObject("price_hourly").getString("gross")
                                ),
                                priceMonth = priceInformation(
                                        net = jsonPrice.getJSONObject("server_types").getJSONObject("prices").getJSONObject("price_monthly").getString("net"),
                                        gross = jsonPrice.getJSONObject("server_types").getJSONObject("prices").getJSONObject("price_monthly").getString("net")
                                )
                        )
                )
        )
        return ServerPrice;
    }
}