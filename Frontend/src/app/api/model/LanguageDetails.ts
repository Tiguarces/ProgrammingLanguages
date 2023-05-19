import { LanguageTrend } from "./LanguageTrend";
import { TiobeIndexStatus } from "./TiobeIndexStatus";

export interface LanguageDetails {
  name: string,
  paradigms: string,
  fileExtensions: string,
  stableRelease: string,
  description: string,
  website: string,
  implementations: string,
  thumbnailPath: string,
  firstAppeared: number,
  tiobeIndex: TiobeIndexStatus,
  trendsList: LanguageTrend[];
}
