import static org.junit.jupiter.api.Assertions.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.xml.bind.annotation.XmlIDREF;

import org.junit.jupiter.api.Test;

import configuration.ConfigXML;
import dataAccess.DataAccess;
import domain.Event;
import exceptions.QuestionAlreadyExist;
import utility.TestUtilityDataAccess;

class getForecastsOfQuestionTest {
	static DataAccess sut = new DataAccess(ConfigXML.getInstance().getDataBaseOpenMode().equals("initialize"));;
	static TestUtilityDataAccess testDA = new TestUtilityDataAccess();
	private Event ev;

	@Test
	// sut.GetForecastsOfQuestion: Hay pronosticos de una pregunta.

	void testGetForecastsOfQuestion() {
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
			Date oneDate = sdf.parse("05/10/2022");
			String eventText = "Event Text";
			String queryText = "Query Text";
			Float betMinimum = 2f;
			int questiontype = 1;
			String pmultiply = "2";

			testDA.open();
			ev = testDA.addEventWithQuestion(eventText, oneDate, queryText, betMinimum,questiontype,pmultiply);
			testDA.close();


			// invoke System Under Test (sut) and Assert
			assertThrows(QuestionAlreadyExist.class, () -> sut.getForecastsOfQuestion(ev.getQuestions().firstElement()));

		} catch (ParseException e) {
			fail("It should be correct: check the date format");
		}

		// Remove the created objects in the database (cascade removing)
		testDA.open();
		boolean b = testDA.removeEvent(ev);
		System.out.println("Removed event " + b);
		testDA.close();

	}
}

