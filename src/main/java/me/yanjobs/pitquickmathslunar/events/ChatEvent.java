package me.yanjobs.pitquickmathslunar.events;

import java.util.Timer;

import java.util.TimerTask;
import java.util.concurrent.ThreadLocalRandom;
import net.weavemc.loader.api.event.ChatReceivedEvent;
import net.weavemc.loader.api.event.SubscribeEvent;

import net.minecraft.client.Minecraft;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
public class ChatEvent {
	private Minecraft mc = Minecraft.getMinecraft();
	
    public static double simpleRandom(final double min, final double max) {
        double x = min;
        double y = max;

        if (min == max) {
            return min;
        } else if (min > max) {
            x = max;
            y = min;
        }

        return ThreadLocalRandom.current().nextDouble(x, y);
    }
	@SubscribeEvent
	public void onMathSolveMessage(ChatReceivedEvent event){
		Long millisStarted = System.currentTimeMillis();

		String rawMessage = event.getMessage().getUnformattedText();

		if(rawMessage.matches(".*[+\\-x/*()].*")){
			try{
				String mathProblem = rawMessage.replace(" ", "");
				
				TimerTask task = new TimerTask() {
	                @Override
	                public void run() {

	                    Minecraft.getMinecraft().thePlayer.sendChatMessage("" + (int) eval(mathProblem.replace("x", "*")));
	                    
	    				Long millisToSolve = System.currentTimeMillis() - millisStarted;
	    				mc.thePlayer.addChatMessage(new ChatComponentText(EnumChatFormatting.GREEN + "Solved this math problem in " + millisToSolve + "ms."));
	                }
	            };
	            Timer timer = new Timer("Timer");
	            long delay = (long) simpleRandom(0, 100);
	            // Timing the task with the delay created above
	            timer.schedule(task, delay);	        
			}
			catch (Exception ex){
				return;
			}
		}
	}
	private double eval(final String str) {
		return new Object() {
			int pos = -1, ch;

			void nextChar() {
				ch = (++pos < str.length()) ? str.charAt(pos) : -1;
			}
			boolean eat(int charToEat) {
				while (ch == ' ') nextChar();
				if (ch == charToEat) {
					nextChar();
					return true;
				}
				return false;
			}
			double parse() {
				nextChar();
				double x = parseExpression();
				if (pos < str.length()) throw new RuntimeException("Unexpected: " + (char)ch);
				return x;
			}
			double parseExpression() {
				double x = parseTerm();
				for (;;) {
					if      (eat('+')) x += parseTerm(); // addition
					else if (eat('-')) x -= parseTerm(); // subtraction
					else return x;
				}
			}
			double parseTerm() {
				double x = parseFactor();
				for (;;) {
					if      (eat('*')) x *= parseFactor(); // multiplication
					else if (eat('/')) x /= parseFactor(); // division
					else return x;
				}
			}
			double parseFactor() {
				if (eat('+')) return parseFactor(); // unary plus
				if (eat('-')) return -parseFactor(); // unary minus

				double x;
				int startPos = this.pos;
				if (eat('(')) { // parentheses
					x = parseExpression();
					eat(')');
				} else if ((ch >= '0' && ch <= '9') || ch == '.') { // numbers
					while ((ch >= '0' && ch <= '9') || ch == '.') nextChar();
					x = Double.parseDouble(str.substring(startPos, this.pos));
				} else if (ch >= 'a' && ch <= 'z') { // functions
					while (ch >= 'a' && ch <= 'z') nextChar();
					String func = str.substring(startPos, this.pos);
					x = parseFactor();
					if (func.equals("sqrt")) x = Math.sqrt(x);
					else if (func.equals("sin")) x = Math.sin(Math.toRadians(x));
					else if (func.equals("cos")) x = Math.cos(Math.toRadians(x));
					else if (func.equals("tan")) x = Math.tan(Math.toRadians(x));
					else throw new RuntimeException("Unknown function: " + func);
				} else {
					throw new RuntimeException("Unexpected: " + (char)ch);
				}

				if (eat('^')) x = Math.pow(x, parseFactor()); // exponentiation

				return x;
			}
		}.parse();
	}
}
