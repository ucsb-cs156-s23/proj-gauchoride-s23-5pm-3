import { render, waitFor } from "@testing-library/react";
import Footer,{space} from "main/components/Nav/Footer";

describe("Footer tests", () => {

  //Test taken from the https://github.com/ucsb-cs156/proj-courses repo
    test("space stands for a space", () => {
    expect(space).toBe(" ");
    });

    test("renders correctly ", async () => {
        const { getByText } = render(
            <Footer />
        );
        await waitFor(() => expect(getByText(/This is a sample webapp using React with a Spring Boot backend./)).toBeInTheDocument());
    });

    test("Links are correct", async () => {
        const {getByTestId} = render(<Footer />)
    
        await waitFor(()=> expect(getByTestId("footerLink")).toHaveAttribute(
          "href",
          "https://www.as.ucsb.edu/sticker-packs"

        ));
    
      });

});


