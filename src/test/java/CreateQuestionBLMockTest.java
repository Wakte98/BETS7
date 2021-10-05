import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import org.junit.Assert;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.mockito.internal.matchers.Null;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;


import businessLogic.BLFacade;
import businessLogic.BLFacadeImplementation;
import dataAccess.DataAccess;
import domain.Event;
import domain.Question;
import exceptions.EventFinished;
import exceptions.QuestionAlreadyExist;


class CreateQuestionBLMockTest {
	DataAccess dataAccess = Mockito.mock(DataAccess.class);
	Event mockedEvent = Mockito.mock(Event.class);

	BLFacade sut = new BLFacadeImplementation(dataAccess);

	@SuppressWarnings("unchecked")
	@DisplayName("sut.createQuestion: The event has one question with a queryText.")
	@Test
	void test1() {
		try {
			// define paramaters
			SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
			Date oneDate = sdf.parse("05/10/2022");

			try {
				// configure Mock
				Mockito.doReturn(oneDate).when(mockedEvent).getEventDate();
				Mockito.when(dataAccess.createQuestion(Mockito.any(Event.class), Mockito.any(String.class),	Mockito.any(Float.class),Mockito.any(Integer.class),Mockito.any(String.class))).thenThrow(QuestionAlreadyExist.class);

				// invoke System Under Test (sut)
				String queryText = "Query Text";
				Float betMinimum = 2f;
				String multi = "2";
				int type = 1;
				assertThrows(QuestionAlreadyExist.class, ()-> sut.createQuestion(mockedEvent, queryText, betMinimum,type,multi));

			} catch (QuestionAlreadyExist e) {
				// if the program goes to this point fail, the first createQuestion of Mock
				fail("Not possible");
			} 
		} catch (ParseException e) {
			fail("It should be correct: check the date format");
		}

	}

	@Test
	@DisplayName("sut.createQuestion: The event has NOT a question with a queryText.")
	void test2() {
		try {
			// define paramaters
			SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
			Date oneDate = sdf.parse("05/10/2022");

			// configure Mock
			Mockito.doReturn(oneDate).when(mockedEvent).getEventDate();
			Float betMinimum = 2f;
			String multi = "2";
			int type = 1;
			try {
				
				Mockito.when(dataAccess.createQuestion(Mockito.any(Event.class), Mockito.any(String.class),	Mockito.any(Float.class),Mockito.any(Integer.class),
						Mockito.any(String.class))).thenThrow(QuestionAlreadyExist.class);

				// invoke System Under Test (sut)
				sut.createQuestion(mockedEvent,null, betMinimum,type,multi);

				// verify the results
				ArgumentCaptor<String> questionStringCaptor = ArgumentCaptor.forClass(String.class);


				Mockito.verify(dataAccess, Mockito.times(1)).createQuestion(Mockito.any(Event.class),
						questionStringCaptor.capture(), Mockito.any(Float.class),Mockito.any(int.class), Mockito.any(String.class));

				assertNull(questionStringCaptor.getValue());


			} catch (QuestionAlreadyExist e) {
				fail("Mock DataAccess should not raise the exception QuestionAlreadyExist");
			} catch (EventFinished e) {
				fail("Mock DataAccess should not raise the exception EventFinished");
			}
		} catch (ParseException e) {
			fail("It should be correct: check the date format");
		}
	}

	@Test
	@DisplayName(" sut.createQuestion: The event is null.")
	void test3() {

		try {
			// define paramaters
			SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
			Date oneDate = sdf.parse("05/10/2022");
			int oneEvent = 5;
			String oneDescription = "evento futbol";
			
			// configure Mock
			Mockito.doReturn(oneDate).when(mockedEvent).getEventDate();
			Mockito.doReturn(oneEvent).when(mockedEvent).getEventNumber();
			Mockito.doReturn(oneDescription).when(mockedEvent).getDescription();

			try {
				String queryText = "Query Text";
				Float betMinimum = 2f;
				String multi = "2";
				int type = 1;
								
				// invoke System Under Test (sut)
				Question q = sut.createQuestion(mockedEvent, queryText, betMinimum,type,multi);

				ArgumentCaptor<Event> EventCaptor = ArgumentCaptor.forClass(Event.class);


				Mockito.verify(dataAccess, Mockito.times(1)).createQuestion(EventCaptor.capture(),
						Mockito.any(String.class), Mockito.any(Float.class),Mockito.any(int.class), Mockito.any(String.class));

				
				assertNull(q);

			} catch (QuestionAlreadyExist e) {
				fail("Mock DataAccess should not raise the exception QuestionAlreadyExist");
			} catch (EventFinished e) {
				fail("Mock DataAccess should not raise the exception EventFinished");
			}
		} catch (ParseException e) {
			fail("It should be correct: check the date format");
		}
	}
}
