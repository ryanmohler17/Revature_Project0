package com.ryan.data;

import com.ryan.models.Client;
import com.ryan.models.Employee;
import com.ryan.models.User;
import org.junit.Test;
import org.mockito.Mockito;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.concurrent.atomic.AtomicReference;

import static org.junit.Assert.*;

public class FileUserAccessTest {

    @Test
    public void testSaveUser() throws IOException {
        BufferedWriter mockedWriter = Mockito.mock(BufferedWriter.class);

        User user = new Employee();
        user.setUserName("user");
        user.setFirstName("First");
        user.setLastName("Last");
        user.setPassword("pass");
        user.setEmailAddress("email@example.com");
        user.setId(0);

        UserDataAccess userDataAccess = new FileUserAccess();
        ((FileUserAccess)userDataAccess).setWriter(((FileUserAccess) userDataAccess).getUsersFile(), mockedWriter);

        AtomicReference<String> written = new AtomicReference<>();

        Mockito.when(mockedWriter.append(Mockito.anyString())).then(invocation -> {
            written.set(invocation.getArgument(0).toString());
            return mockedWriter;
        });

        userDataAccess.saveItem(user);

        assertEquals("Expected User format does not match written", ((FileUserAccess) userDataAccess).formatUser(user, 1), written.get());
    }

    @Test
    public void testSaveMultipleUsers() throws IOException {
        BufferedWriter mockedWriter = Mockito.mock(BufferedWriter.class);

        User user1 = new Employee();
        user1.setUserName("user");
        user1.setFirstName("First");
        user1.setLastName("Last");
        user1.setPassword("pass");
        user1.setEmailAddress("email@example.com");
        user1.setId(0);

        User user2 = new Client();
        user2.setUserName("user2");
        user2.setFirstName("First");
        user2.setLastName("Last");
        user2.setPassword("pass");
        user2.setEmailAddress("email@example.com");
        user2.setId(1);

        UserDataAccess userDataAccess = new FileUserAccess();
        ((FileUserAccess)userDataAccess).setWriter(((FileUserAccess) userDataAccess).getUsersFile(), mockedWriter);

        AtomicReference<String> written = new AtomicReference<>();

        Mockito.when(mockedWriter.append(Mockito.anyString())).then(invocation -> {
            written.set((written.get() == null ? "" : written.get()) + invocation.getArgument(0).toString());
            return mockedWriter;
        });

        userDataAccess.saveItem(user1);
        userDataAccess.saveItem(user2);

        String expected = ((FileUserAccess) userDataAccess).formatUser(user1, 1) + ((FileUserAccess) userDataAccess).formatUser(user2, 2);

        assertEquals("Expected User format does not match written", expected, written.get());
    }

}
