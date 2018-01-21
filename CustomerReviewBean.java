import java.util.ArrayList;
import java.util.List;

import de.hybris.platform.customerreview.impl.ProductModel;

/**
 * @author ShradhaSaxena
 *
 */
public class CustomerReviewBean {

	/**
	 * Calculates a product’s total number of customer reviews whose 
	 * ratings are within a given range (inclusive). 
	 * @param ratingStartRange, ratingEndRange
	 * @return int count
	 */	
	public int getTotalNumOfCustomerReviews(Double ratingStartRange, Double ratingEndRange) {

		List<CustomerReviewModel> customerReviewModelList = getCustomerReviewService().getReviewsForProduct(ProductModel product);
		int count = 0;
		for (CustomerReviewModel customerReviewModel:customerReviewModelList) {
			if (customerReviewModel.getCustomerRating() >= ratingStartRange && customerReviewModel.getCustomerRating() <= ratingEndRange) {
				count = count + 1;
			}
		}	
		return count;
	}
	
	/**
	 * Calls for the validation method for comments and ratings and 
	 * then call to the service method to save the customer review
	 * @param ProductModel
	 * @return CustomerReviewModel
	 */	
	public CustomerReviewModel createCustomerReview(ProductModel product) {
		CustomerReviewModel customerReviewModel = new CustomerReviewModel();
		
		try {
			
			checkForCurseWords(product.getCustomerComment());
			
			checkForRatings(product.getCustomerRating());
			//call to the service method to create the customer review
			customerReviewModel = getCustomerReviewService().createCustomerReview();
			
		} catch (Exception e) {				
			e.printStackTrace();
		}
		
	}
	
	/**
	 * Validates customer comments against a list of curse words
	 * @param customerComment
	 * @return void
	 * @throws Exception
	 */	
	public void checkForCurseWords(String customerComment) throws Exception{
		
		//List of curse words can be stored in a database table and read from there into a list of String
		List<String> listCurseWords = getCustomerReviewService().readListOfCurseWords();
		//						OR
		//List of curse words can be stored in a constants file like CustomerReviewConstants and read from there
		//Example of code in CustomerReviewConstants file:  
		//public static final String[] CURSE_WORDS = {"curseWordOne","curseWordTwo","curseWordThree"};
		List<String> listCurseWords = CustomerReviewConstants.getInstance().CURSE_WORDS;
		
		for (String curseWord: listCurseWords) {
			if (customerComment.toLowerCase().contains(curseWord.toLowerCase())) {
				throw new Exception("Comment contains Curse word "+customerComment);
			}
		}
		
	}
	
	/**
	 * Validates customer rating and throws exception if it is less than 0.0D
	 * @param customerRating
	 * @return void
	 * @throws Exception
	 */	
	public void checkForRatings(Double customerRating) throws Exception{
		
		if (customerRating < CustomerReviewConstants.getInstance().MINRATING) {
			throw new Exception("Customer Rating is below 0 "+customerRating);
		}
	}
	
}
