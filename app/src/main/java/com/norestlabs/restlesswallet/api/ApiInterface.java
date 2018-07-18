package com.norestlabs.restlesswallet.api;

import com.norestlabs.restlesswallet.models.request.ShiftRequest;
import com.norestlabs.restlesswallet.models.response.BitcoinFeeResponse;
import com.norestlabs.restlesswallet.models.response.CoinResponse;
import com.norestlabs.restlesswallet.models.response.ConversionResponse;
import com.norestlabs.restlesswallet.models.response.EtherChainResponse;
import com.norestlabs.restlesswallet.models.response.MarketInfoResponse;
import com.norestlabs.restlesswallet.models.response.ShiftResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiInterface {

    @GET("getcoins")
    Call<CoinResponse> getCoins();

    @GET("marketinfo/{pair}")
    Call<MarketInfoResponse> getMarketInfo(@Path("pair") String pair);

    @POST("shift")
    Call<ShiftResponse> shift(@Body ShiftRequest request);

    @GET("gasPriceOracle")
    Call<EtherChainResponse> getETHFee();

    @GET("recommended")
    Call<BitcoinFeeResponse> getBTCFee();

    @GET("ticker")
    Call<ConversionResponse> getCoinMarketCap(@Query("convert") String type);
}
