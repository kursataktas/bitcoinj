/*
 * Copyright by the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.bitcoinj.base;

import java.util.Arrays;
import java.util.Locale;
import java.util.Optional;

import static org.bitcoinj.base.Coin.COIN;

/**
 * A convenient {@code enum} representation of a Bitcoin network.
 * <p>
 * Note that the name of each {@code enum} constant is defined in <i>uppercase</i> as is the convention in Java.
 * However, the <q>canonical</q> representation in <b>bitcoinj</b> for user-facing display and input
 * of Bitcoin network names is <i>lowercase</i> (e.g. as a command-line parameter.)
 * Implementations should use the {@link #toString()} method for output and the {@link #fromString(String)}
 * method for input of network values.
 */
public enum BitcoinNetwork implements Network {
    MAIN("org.bitcoin.production", new String[] { "mainnet", "prod" }),
    TEST("org.bitcoin.test", new String[] { "testnet" }),
    SIGNET("org.bitcoin.signet", new String[] { "sig" }),
    REGTEST("org.bitcoin.regtest", new String[0]);

    /**
     * The maximum number of coins to be generated
     */
    private static final long MAX_COINS = 21000000;

    /**
     * The maximum money to be generated
     */
    public static final Coin MAX_MONEY = COIN.multiply(MAX_COINS);

    /** The ID string for the main, production network where people trade things. */
    public static final String ID_MAINNET = MAIN.id();
    /** The ID string for the testnet. */
    public static final String ID_TESTNET = TEST.id();
    /** The ID string for the signet. */
    public static final String ID_SIGNET = SIGNET.id();
    /** The ID string for regtest mode. */
    public static final String ID_REGTEST = REGTEST.id();
    /** The ID string for the Unit test network -- there is no corresponding {@code enum}. */
    public static final String ID_UNITTESTNET = "org.bitcoinj.unittest";

    private final String id;
    private final String[] alternateNames;

    BitcoinNetwork(String networkId, String[] alternateNames) {
        this.id = networkId;
        this.alternateNames = alternateNames;
    }

    /**
     * Return the canonical, lowercase, user-facing {@code String} for an {@code enum}
     * @return canonical lowercase value
     */
    @Override
    public String toString() {
        return name().toLowerCase(Locale.ROOT);
    }

    /**
     * Return the network ID string (previously specified in {@code NetworkParameters})
     *
     * @return The network ID string
     */
    @Override
    public String id() {
        return id;
    }

    @Override
    public boolean hasMaxMoney() {
        return true;
    }

    @Override
    public Coin maxMoney() {
        return MAX_MONEY;
    }

    /**
     * Find the {@code BitcoinNetwork} from a name string, e.g. "main", "test" or "signet".
     * A number of common alternate names are allowed too, e.g. "mainnet" or "prod".
     * @param nameString A name string
     * @return An {@code Optional} containing the matching enum or empty
     */
    public static Optional<BitcoinNetwork> fromString(String nameString) {
        final Optional<BitcoinNetwork> canonicalName = findCanonicalName(nameString);
        return canonicalName.isPresent()
                ? canonicalName
                : findAlternateName(nameString);
    }

    private static Optional<BitcoinNetwork> findCanonicalName(String nameString) {
        return Arrays.stream(values())
                .filter(n -> n.toString().equals(nameString))
                .findFirst();
    }

    private static Optional<BitcoinNetwork> findAlternateName(String nameString) {
        return Arrays.stream(values())
                .filter(n -> Arrays.stream(n.alternateNames).anyMatch(a -> a.equals(nameString)))
                .findFirst();
    }

    /**
     * Find the {@code BitcoinNetwork} from an ID String
     * <p>
     * Note: {@link #ID_UNITTESTNET} is not supported as an enum
     * @param idString specifies the network
     * @return An {@code Optional} containing the matching enum or empty
     */
    public static Optional<BitcoinNetwork> fromIdString(String idString) {
        return Arrays.stream(values())
                .filter(n -> n.id.equals(idString))
                .findFirst();
    }
}