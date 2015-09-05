/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2015 Gareth Jon Lynch
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of
 * this software and associated documentation files (the "Software"), to deal in
 * the Software without restriction, including without limitation the rights to
 * use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of
 * the Software, and to permit persons to whom the Software is furnished to do so,
 * subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS
 * FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR
 * COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER
 * IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN
 * CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package com.gazbert.bxbot.core.api.trading;

import com.gazbert.bxbot.core.api.strategy.StrategyException;

import java.math.BigDecimal;
import java.util.List;

/**
 * <p>
 * BXBot's Trading API.
 * </p>
 *
 * <p>
 * This is what Trading Strategies use to trade.
 * </p>
 *
 * <p>
 * Exchange Adapters provide their own implementation of the API for the trading they wish to trade on.
 * </p>
 *
 * <p>
 * This version of the Trading API only supports <a href="http://www.investopedia.com/terms/l/limitorder.asp">limit orders</a>
 * traded at the <a href="http://www.investopedia.com/terms/s/spotprice.asp">spot price</a>.
 * It does not support futures trading or margin trading... yet.
 * </p>
 *
 * @author gazbert
 */
public interface TradingApi {

    /**
     * Returns the current version of the API.
     * @return the API version.
     */
    static String getVersion() {
        return "1.0";
    }

    /**
     * Returns the API implementation name.
     * @return the API implementation name.
     */
    String getImplName();

    /**
     * Fetches latest <em>market</em> orders for a given market.
     *
     * @param marketId the id of the market.
     * @return the market order book.
     * @throws ExchangeTimeoutException if a timeout occurred trying to connect to the trading. The timeout limit is
     *                                  implementation specific for each Exchange Adapter; see the documentation for the
     *                                  adapter you are using. You could retry the API call, or exit from your Trading Strategy
     *                                  and let the Trading Engine execute your Trading Strategy at the next trade cycle.
     *                                  This allows the you to recover from temporary network issues.
     * @throws TradingApiException      if the API call failed for any reason other than a timeout. This means something
     *                                  bad as happened; you would probably want to wrap this exception in a 
     *                                  {@link StrategyException} and let the Trading Engine shutdown the bot immediately 
     *                                  to prevent unexpected losses.
     */
    MarketOrderBook getMarketOrders(String marketId) throws ExchangeTimeoutException, TradingApiException;

    /**
     * Fetches <em>your</em> current open orders, i.e. the orders placed by the bot.
     *
     * @param marketId the id of the market.
     * @return your current open orders.
     * @throws ExchangeTimeoutException if a timeout occurred trying to connect to the trading. The timeout limit is
     *                                  implementation specific for each Exchange Adapter; see the documentation for the
     *                                  adapter you are using. You could retry the API call, or exit from your Trading Strategy
     *                                  and let the Trading Engine execute your Trading Strategy at the next trade cycle.
     *                                  This allows the you to recover from temporary network issues.
     * @throws TradingApiException      if the API call failed for any reason other than a timeout. This means something
     *                                  bad as happened; you would probably want to wrap this exception in a
     *                                  {@link StrategyException} and let the Trading Engine shutdown the bot immediately
     *                                  to prevent unexpected losses.
     */
    List<OpenOrder> getYourOpenOrders(String marketId) throws ExchangeTimeoutException, TradingApiException;

    /**
     * Places an order on the trading.
     *
     * @param marketId  the id of the market.
     * @param orderType Value must be {@link OrderType#BUY} or {@link OrderType#SELL}.
     * @param quantity  amount of units you are buying/selling in this order.
     * @param price     the price per unit you are buying/selling at.
     * @return the id of the order.
     * @throws ExchangeTimeoutException if a timeout occurred trying to connect to the trading. The timeout limit is
     *                                  implementation specific for each Exchange Adapter; see the documentation for the
     *                                  adapter you are using. You could retry the API call, or exit from your Trading Strategy
     *                                  and let the Trading Engine execute your Trading Strategy at the next trade cycle.
     *                                  This allows the you to recover from temporary network issues.
     * @throws TradingApiException      if the API call failed for any reason other than a timeout. This means something
     *                                  bad as happened; you would probably want to wrap this exception in a
     *                                  {@link StrategyException} and let the Trading Engine shutdown the bot immediately
     *                                  to prevent unexpected losses.
     */
    String createOrder(String marketId, OrderType orderType, BigDecimal quantity, BigDecimal price)
            throws ExchangeTimeoutException, TradingApiException;

    /**
     * Cancels your existing order on the trading.
     *
     * @param orderId your order Id.
     * @return true if order cancelled ok, false otherwise.
     * @throws ExchangeTimeoutException if a timeout occurred trying to connect to the trading. The timeout limit is
     *                                  implementation specific for each Exchange Adapter; see the documentation for the
     *                                  adapter you are using. You could retry the API call, or exit from your Trading Strategy
     *                                  and let the Trading Engine execute your Trading Strategy at the next trade cycle.
     *                                  This allows the you to recover from temporary network issues.
     * @throws TradingApiException      if the API call failed for any reason other than a timeout. This means something
     *                                  bad as happened; you would probably want to wrap this exception in a
     *                                  {@link StrategyException} and let the Trading Engine shutdown the bot immediately
     *                                  to prevent unexpected losses.
     */
    boolean cancelOrder(String orderId) throws ExchangeTimeoutException, TradingApiException;

    /**
     * Fetches the latest price for a given market.
     * This is usually in BTC for altcoin markets and USD for BTC/USD markets - see the Exchange Adapter documentation.
     *
     * @param marketId the id of the market.
     * @return the latest market price.
     * @throws ExchangeTimeoutException if a timeout occurred trying to connect to the trading. The timeout limit is
     *                                  implementation specific for each Exchange Adapter; see the documentation for the
     *                                  adapter you are using. You could retry the API call, or exit from your Trading Strategy
     *                                  and let the Trading Engine execute your Trading Strategy at the next trade cycle.
     *                                  This allows the you to recover from temporary network issues.
     * @throws TradingApiException      if the API call failed for any reason other than a timeout. This means something
     *                                  bad as happened; you would probably want to wrap this exception in a
     *                                  {@link StrategyException} and let the Trading Engine shutdown the bot immediately
     *                                  to prevent unexpected losses.
     */
    BigDecimal getLatestMarketPrice(String marketId) throws ExchangeTimeoutException, TradingApiException;

    /**
     * Fetches the balance of your wallets on the trading.
     *
     * @return your wallet balance info.
     * @throws ExchangeTimeoutException if a timeout occurred trying to connect to the trading. The timeout limit is
     *                                  implementation specific for each Exchange Adapter; see the documentation for the
     *                                  adapter you are using. You could retry the API call, or exit from your Trading Strategy
     *                                  and let the Trading Engine execute your Trading Strategy at the next trade cycle.
     *                                  This allows the you to recover from temporary network issues.
     * @throws TradingApiException      if the API call failed for any reason other than a timeout. This means something
     *                                  bad as happened; you would probably want to wrap this exception in a
     *                                  {@link StrategyException} and let the Trading Engine shutdown the bot immediately
     *                                  to prevent unexpected losses.
     */
    BalanceInfo getBalanceInfo() throws ExchangeTimeoutException, TradingApiException;

    /**
     * Returns the trading BUY order fee for a given market id.
     * The returned value is the % of the BUY order that the trading uses to calculate its fee as
     * a {@link BigDecimal}. If the fee is 0.33%, then the {@link BigDecimal} value returned is
     * 0.0033.
     * @param marketId the id of the market.
     * @return the % of the BUY order that the trading uses to calculate its fee as a {@link BigDecimal}.
     * @throws ExchangeTimeoutException if a timeout occurred trying to connect to the trading. The timeout limit is
     *                                  implementation specific for each Exchange Adapter; see the documentation for the
     *                                  adapter you are using. You could retry the API call, or exit from your Trading Strategy
     *                                  and let the Trading Engine execute your Trading Strategy at the next trade cycle.
     *                                  This allows the you to recover from temporary network issues.
     * @throws TradingApiException      if the API call failed for any reason other than a timeout. This means something
     *                                  bad as happened; you would probably want to wrap this exception in a
     *                                  {@link StrategyException} and let the Trading Engine shutdown the bot immediately
     *                                  to prevent unexpected losses.
     */
    BigDecimal getPercentageOfBuyOrderTakenForExchangeFee(String marketId) throws TradingApiException, ExchangeTimeoutException;

    /**
     * Returns the trading SELL order fee for a given market id.
     * The returned value is the % of the SELL order that the trading uses to calculate its fee as a
     * {@link BigDecimal}. If the fee is 0.33%, then the {@link BigDecimal} value returned is
     * 0.0033.
     * @param marketId the id of the market.
     * @return the % of the SELL order that the trading uses to calculate its fee as a {@link BigDecimal}.
     * @throws ExchangeTimeoutException if a timeout occurred trying to connect to the trading. The timeout limit is
     *                                  implementation specific for each Exchange Adapter; see the documentation for the
     *                                  adapter you are using. You could retry the API call, or exit from your Trading Strategy
     *                                  and let the Trading Engine execute your Trading Strategy at the next trade cycle.
     *                                  This allows the you to recover from temporary network issues.
     * @throws TradingApiException      if the API call failed for any reason other than a timeout. This means something
     *                                  bad as happened; you would probably want to wrap this exception in a
     *                                  {@link StrategyException} and let the Trading Engine shutdown the bot immediately
     *                                  to prevent unexpected losses.
     */
    BigDecimal getPercentageOfSellOrderTakenForExchangeFee(String marketId) throws TradingApiException, ExchangeTimeoutException;
}