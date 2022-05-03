package com.bujosa.antares.coinmarket;

import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;

// Documentation for this api: https://coinmarketcap.com/api/documentation/v1/
public class CoinMarketService {

    final private OkHttpClient client;

    public CoinMarketService(OkHttpClient client) {
        this.client = client;
    }

    // Note: Replace for your own key
    public Call getBitcoinPrice() {
        String url = "https://pro-api.coinmarketcap.com/v1/cryptocurrency/listings/latest?limit=1";
        Request request = new Request.Builder()
                .header("X-CMC_PRO_API_KEY", "YOUR API KEY")
                .url(url)
                .build();

        return client.newCall(request);
    }

}

