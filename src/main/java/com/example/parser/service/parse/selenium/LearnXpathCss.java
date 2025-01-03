package com.example.parser.service.parse.selenium;

import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

@Service
@Log4j2
public class LearnXpathCss {
//    https://www.youtube.com/watch?v=ejIiX7--f7w&list=PLZqgWWF4O-ziBZVXN19WcRHPM5DkH672c&index=11
    public void xPath() {

        /**
         <textarea class="gLFyf"                   //textarea[@class='gLFyf']
         aria-controls="Alh6id"                    //textarea[@aria-controls='Alh6id']
         aria-owns="Alh6id"                        //textarea[@aria-owns='Alh6id']
         value="linkedin"                          //textarea[@value='linkedin']
         jsaction="paste:puy29d; mouseenter:MJEKMe; mouseleave:iFHZnf;" //textarea[@jsaction='paste:puy29d; mouseenter:MJEKMe; mouseleave:iFHZnf;']
         aria-label="Пошук"                        //textarea[@aria-label='Пошук']
         placeholder=""                            //textarea[@placeholder='']
         aria-autocomplete="both"                  //textarea[@aria-autocomplete='both']
         aria-expanded="false"                     //textarea[@aria-expanded='false']
         aria-haspopup="false"                     //textarea[@aria-haspopup='false']
         autocapitalize="off"                      //textarea[@autocapitalize='off']
         autocomplete="off"                        //textarea[@autocomplete='off']
         autocorrect="off"                         //textarea[@autocorrect='off']
         id="APjFqb"                               //textarea[@id='APjFqb']
         maxlength="2048"                          //textarea[@maxlength='2048']
         name="q"                                  //textarea[@name='q']
         role="combobox"                           //textarea[@role='combobox']
         rows="1"                                  //textarea[@rows='1']
         spellcheck="false"                        //textarea[@spellcheck='false']
         data-ved="0ahUKEwior_q5p-OKAxWPIRAIHeXnD90Q39UDCAw" //textarea[@data-ved='0ahUKEwior_q5p-OKAxWPIRAIHeXnD90Q39UDCAw']>
         linkedin
         </textarea>
         */
    }

    public void cssPath() {
/**

     <textarea class="gLFyf"                   //textarea[@class='gLFyf']
     aria-controls="Alh6id"                    //textarea[@aria-controls='Alh6id']
     aria-owns="Alh6id"                        //textarea[@aria-owns='Alh6id']
     value="linkedin"                          //textarea[@value='linkedin']
     jsaction="paste:puy29d; mouseenter:MJEKMe; mouseleave:iFHZnf;" //textarea[@jsaction='paste:puy29d; mouseenter:MJEKMe; mouseleave:iFHZnf;']
     aria-label="Пошук"                        //textarea[@aria-label='Пошук']
     placeholder=""                            //textarea[@placeholder='']
     aria-autocomplete="both"                  //textarea[@aria-autocomplete='both']
     aria-expanded="false"                     //textarea[@aria-expanded='false']
     aria-haspopup="false"                     //textarea[@aria-haspopup='false']
     autocapitalize="off"                      //textarea[@autocapitalize='off']
     autocomplete="off"                        //textarea[@autocomplete='off']
     autocorrect="off"                         //textarea[@autocorrect='off']
     id="APjFqb"                               //textarea[@id='APjFqb']
     maxlength="2048"                          //textarea[@maxlength='2048']
     name="q"                                  //textarea[@name='q']
     role="combobox"                           //textarea[@role='combobox']
     rows="1"                                  //textarea[@rows='1']
     spellcheck="false"                        //textarea[@spellcheck='false']
     data-ved="0ahUKEwior_q5p-OKAxWPIRAIHeXnD90Q39UDCAw" //textarea[@data-ved='0ahUKEwior_q5p-OKAxWPIRAIHeXnD90Q39UDCAw']>
     linkedin
     </textarea>
 */
    }

    public void allVariants(){
        /**
         <div>Content inside div</div> <!-- XPath: //div | CSS: div -->
         <p class="my-class">Text with class</p> <!-- XPath: //p[@class='my-class'] | CSS: .my-class -->
         <input id="my-id" type="text" value="example"> <!-- XPath: //input[@id='my-id'] | CSS: #my-id -->
         <input type="text" value="example"> <!-- XPath: //input[@type='text'] | CSS: input[type="text"] -->
         <div>
         <p>First paragraph inside div</p> <!-- XPath: //div/p | CSS: div p -->
         <p>Second paragraph inside div</p> <!-- XPath: //div/p | CSS: div p -->
         </div>
         <div>
         <p>Direct child paragraph inside div</p> <!-- XPath: //div/p[1] | CSS: div > p -->
         </div>
         <h2>Heading 2</h2>
         <p>Paragraph after h2</p> <!-- XPath: //h2/following-sibling::p | CSS: h2 + p -->
         <h1>Heading 1</h1>
         <h2>Heading 2</h2>
         <h3>Heading 3</h3> <!-- XPath: //h1 | //h2 | //h3 | CSS: h1, h2, h3 -->
         <a href="#">Link with hover effect</a> <!-- XPath: //a[@href] | CSS: a:hover -->
         <input type="text" placeholder="Focus me"> <!-- XPath: //input[@type='text'][@placeholder='Focus me'] | CSS: input:focus -->
         <p>Some text with <span>styled first letter</span></p> <!-- XPath: //p[span] | CSS: p::first-letter -->
         */
    }

    public void idClassClassAtribute() {
        /**
            <div id="my-id" class="class1 class2" data-type="example">Content</div>
           //div[@id='my-id' and contains(@class, 'class1') and contains(@class, 'class2') and @data-type='example']
         div#my-id.class1.class2[data-type='example']

         <div id="my-id" class="class1 class2" data-type="example" data-role="admin">Content</div>
         //div[@id='my-id' and contains(@class, 'class1') and contains(@class, 'class2') and @data-type='example' and @data-role='admin']
         div#my-id.class1.class2[data-type='example'][data-role='admin']

         <div class="class1 class2">Hello World</div>
         //div[contains(@class, 'class1') and contains(@class, 'class2') and text()='Hello World']
         div.class1.class2:contains('Hello World')
         div.class1:contains('Hello World')
         */
    }
}
