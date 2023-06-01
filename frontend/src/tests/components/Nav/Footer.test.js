import { render, waitFor } from "@testing-library/react";
import Footer from "main/components/Nav/Footer";

describe("Footer tests", () => {


    test("renders correctly ", async () => {
        const { getByText } = render(
            <Footer />
        );
        await waitFor(() => expect(getByText(/This is a sample webapp using React with a Spring Boot backend./)).toBeInTheDocument());
    });

    // test("Links are correct", async () => {
    //     render(<Footer />)
    
    //     await waitFor(()=> expect(getByTestId("footerLink")).toHaveAttribute(
    //       "href",
    //       "https://www.as.ucsb.edu/sticker-packs"

    //     ));
    
    //   });

});


