package random;

import static org.junit.Assert.fail;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class TriangleTest {

	private TriangleProb triangleProb;
	private int a,b,c;
	
	@Before
	public void setUp() throws Exception {
		this.a = 3;
		this.b = 4;
		this.c = 5;
		
		this.triangleProb = new TriangleProb();
	}


	@Test
	public void testIsEqui() {
		
		boolean result = this.triangleProb.isEqui(a, b, c);
		
		Assert.assertTrue(result==false);
		
	}

	@Test
	public void testIsIso() {
		
		boolean result = this.triangleProb.isIso(a, b, c);
		
		Assert.assertTrue(result==false);
		
	}

	@Test
	public void testIsScal() {
		
		boolean result = this.triangleProb.isScal(a, b, c);
		
		Assert.assertTrue(result==true);
		
	}

	@Test
	public void testIsRight() {
		
		boolean result = this.triangleProb.isRight(a, b, c);
		
		Assert.assertTrue(result==true);
		
	}

}
