package module.nrlwallet.com.nrlwalletsdk.Stellar.responses;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import module.nrlwallet.com.nrlwalletsdk.Stellar.KeyPair;
import module.nrlwallet.com.nrlwalletsdk.Stellar.responses.effects.*;

import java.lang.reflect.Type;

import module.nrlwallet.com.nrlwalletsdk.Stellar.KeyPair;
import module.nrlwallet.com.nrlwalletsdk.Stellar.responses.effects.AccountCreatedEffectResponse;
import module.nrlwallet.com.nrlwalletsdk.Stellar.responses.effects.AccountCreditedEffectResponse;
import module.nrlwallet.com.nrlwalletsdk.Stellar.responses.effects.AccountDebitedEffectResponse;
import module.nrlwallet.com.nrlwalletsdk.Stellar.responses.effects.AccountFlagsUpdatedEffectResponse;
import module.nrlwallet.com.nrlwalletsdk.Stellar.responses.effects.AccountHomeDomainUpdatedEffectResponse;
import module.nrlwallet.com.nrlwalletsdk.Stellar.responses.effects.AccountInflationDestinationUpdatedEffectResponse;
import module.nrlwallet.com.nrlwalletsdk.Stellar.responses.effects.AccountRemovedEffectResponse;
import module.nrlwallet.com.nrlwalletsdk.Stellar.responses.effects.AccountThresholdsUpdatedEffectResponse;
import module.nrlwallet.com.nrlwalletsdk.Stellar.responses.effects.DataCreatedEffectResponse;
import module.nrlwallet.com.nrlwalletsdk.Stellar.responses.effects.DataRemovedEffectResponse;
import module.nrlwallet.com.nrlwalletsdk.Stellar.responses.effects.DataUpdatedEffectResponse;
import module.nrlwallet.com.nrlwalletsdk.Stellar.responses.effects.EffectResponse;
import module.nrlwallet.com.nrlwalletsdk.Stellar.responses.effects.OfferCreatedEffectResponse;
import module.nrlwallet.com.nrlwalletsdk.Stellar.responses.effects.OfferRemovedEffectResponse;
import module.nrlwallet.com.nrlwalletsdk.Stellar.responses.effects.OfferUpdatedEffectResponse;
import module.nrlwallet.com.nrlwalletsdk.Stellar.responses.effects.SignerCreatedEffectResponse;
import module.nrlwallet.com.nrlwalletsdk.Stellar.responses.effects.SignerRemovedEffectResponse;
import module.nrlwallet.com.nrlwalletsdk.Stellar.responses.effects.SignerUpdatedEffectResponse;
import module.nrlwallet.com.nrlwalletsdk.Stellar.responses.effects.TradeEffectResponse;
import module.nrlwallet.com.nrlwalletsdk.Stellar.responses.effects.TrustlineAuthorizedEffectResponse;
import module.nrlwallet.com.nrlwalletsdk.Stellar.responses.effects.TrustlineCreatedEffectResponse;
import module.nrlwallet.com.nrlwalletsdk.Stellar.responses.effects.TrustlineDeauthorizedEffectResponse;
import module.nrlwallet.com.nrlwalletsdk.Stellar.responses.effects.TrustlineRemovedEffectResponse;
import module.nrlwallet.com.nrlwalletsdk.Stellar.responses.effects.TrustlineUpdatedEffectResponse;

class EffectDeserializer implements JsonDeserializer<EffectResponse> {
  @Override
  public EffectResponse deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
    // Create new Gson object with adapters needed in Operation
    Gson gson = new GsonBuilder()
            .registerTypeAdapter(KeyPair.class, new KeyPairTypeAdapter().nullSafe())
            .create();

    int type = json.getAsJsonObject().get("type_i").getAsInt();
    switch (type) {
      // Account effects
      case 0:
        return gson.fromJson(json, AccountCreatedEffectResponse.class);
      case 1:
        return gson.fromJson(json, AccountRemovedEffectResponse.class);
      case 2:
        return gson.fromJson(json, AccountCreditedEffectResponse.class);
      case 3:
        return gson.fromJson(json, AccountDebitedEffectResponse.class);
      case 4:
        return gson.fromJson(json, AccountThresholdsUpdatedEffectResponse.class);
      case 5:
        return gson.fromJson(json, AccountHomeDomainUpdatedEffectResponse.class);
      case 6:
        return gson.fromJson(json, AccountFlagsUpdatedEffectResponse.class);
      case 7:
        return gson.fromJson(json, AccountInflationDestinationUpdatedEffectResponse.class);
      // Signer effects
      case 10:
        return gson.fromJson(json, SignerCreatedEffectResponse.class);
      case 11:
        return gson.fromJson(json, SignerRemovedEffectResponse.class);
      case 12:
        return gson.fromJson(json, SignerUpdatedEffectResponse.class);
      // Trustline effects
      case 20:
        return gson.fromJson(json, TrustlineCreatedEffectResponse.class);
      case 21:
        return gson.fromJson(json, TrustlineRemovedEffectResponse.class);
      case 22:
        return gson.fromJson(json, TrustlineUpdatedEffectResponse.class);
      case 23:
        return gson.fromJson(json, TrustlineAuthorizedEffectResponse.class);
      case 24:
        return gson.fromJson(json, TrustlineDeauthorizedEffectResponse.class);
      // Trading effects
      case 30:
        return gson.fromJson(json, OfferCreatedEffectResponse.class);
      case 31:
        return gson.fromJson(json, OfferRemovedEffectResponse.class);
      case 32:
        return gson.fromJson(json, OfferUpdatedEffectResponse.class);
      case 33:
        return gson.fromJson(json, TradeEffectResponse.class);
      // Data effects
      case 40:
        return gson.fromJson(json, DataCreatedEffectResponse.class);
      case 41:
        return gson.fromJson(json, DataRemovedEffectResponse.class);
      case 42:
        return gson.fromJson(json, DataUpdatedEffectResponse.class);
      default:
        throw new RuntimeException("Invalid operation type");
    }
  }
}
