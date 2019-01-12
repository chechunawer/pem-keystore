/*******************************************************************************
 * Copyright (c) 2018 Red Hat Inc and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Jens Reimann - initial API and implementation
 *******************************************************************************/
package de.dentrassi.crypto.pem;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.Security;

import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class PemKeyStoreTest {

	@BeforeAll
	public static void setup() {
	}

	protected void testWithProvider(final ThrowingCallable callable) throws Throwable {

		// before running the test, there should be no "PEM" provider yet

		assertThatThrownBy(() -> KeyStore.getInstance("PEM")).isInstanceOf(KeyStoreException.class);

		Security.addProvider(new PemKeyStoreProvider());

		try {
			callable.call();
		} finally {
			Security.removeProvider("PEM");
		}

		// after running the test, there should again be no "PEM" provider any more

		assertThatThrownBy(() -> KeyStore.getInstance("PEM")).isInstanceOf(KeyStoreException.class);
	}

	@Test
	public void testGetInstance1() throws Throwable {

		testWithProvider(() -> {
			KeyStore.getInstance("PEM");
		});

	}

	@Test
	public void testGetInstance2() throws Throwable {

		testWithProvider(() -> {
			KeyStore.getInstance("PEM", "PEM");
		});

	}

	@Test
	public void testGetInstance3() throws Throwable {

		testWithProvider(() -> {
			KeyStore.getInstance("PEM", new PemKeyStoreProvider());
		});

	}

}
